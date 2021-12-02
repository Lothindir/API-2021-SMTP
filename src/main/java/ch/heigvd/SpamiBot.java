/**
 * Authors  : Anthony Coke, Francesco Monti
 * Date     : 2021-11-28
 */
package ch.heigvd;

import ch.heigvd.config.ConfigurationManager;
import ch.heigvd.prank.Prank;
import ch.heigvd.prank.PrankManager;
import ch.heigvd.smtp.SmtpClient;
import java.util.List;

/**
 * Implementation of a spam bot in Java
 */
public class SpamiBot {

    /**
     * Does the prank ...
     * @param args args[0] is the path where the config files are located
     */
    public static void main(String... args) {

        // Fetches the config
        ConfigurationManager confman;
        if (args.length == 1)
            confman = new ConfigurationManager(args[0]);
        else
            confman = new ConfigurationManager();

        // Generates the prank(s)
        PrankManager pman = new PrankManager(confman);
        List<Prank> pranks = pman.generatePranks();

        // Sends the e-mail(s) to the SMTP server with a SMTP client
        SmtpClient smtpClient = new SmtpClient(confman.getSmtpServerAddress(), confman.getSmtpServerPort());
        for (Prank p : pranks) {
            smtpClient.sendMail(p.getMail());
        }
    }
}
