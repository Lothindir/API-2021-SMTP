package ch.heigvd;

import java.util.List;
import java.util.logging.Logger;

import ch.heigvd.config.ConfigurationManager;
import ch.heigvd.prank.Prank;
import ch.heigvd.prank.PrankManager;
import ch.heigvd.smtp.SmtpClient;

/**
 * Implementation of a SpamiBot. It will run the prank campaign.
 *
 * @author Anthony Coke
 * @author Francesco Monti
 */
public class SpamiBot {
   private final static Logger LOG = Logger.getLogger(PrankManager.class.getName());

   /**
    * Does the prank ...
    * 
    * @param args args[0] is the path where the config files are located.
    */
   public static void main(String... args) {
      System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

      // Fetches the config
      ConfigurationManager confman;
      try {
         if (args.length == 1)
            confman = new ConfigurationManager(args[0]);
         else
            confman = new ConfigurationManager();
      } catch (RuntimeException e) {
         LOG.severe(e.getMessage());
         LOG.severe("Application halted!");
         return;
      }

      // Generates the prank(s)
      PrankManager pman = new PrankManager(confman);
      List<Prank> pranks = pman.generatePranks();

      // Sends the e-mail(s) to the SMTP server with a SMTP client
      try (SmtpClient smtpClient = new SmtpClient(confman.getSmtpServerAddress(), confman.getSmtpServerPort(),
            confman.getSmtpUser(), confman.getSmtpPassword())) {
         for (Prank p : pranks) {
            smtpClient.sendMail(p.getMail());
         }
      } catch (Exception e) {
         LOG.severe("An error occurred while sending the mails\n" + e.getMessage());
      }
   }
}
