package ch.heigvd;

import ch.heigvd.config.ConfigurationManager;
import ch.heigvd.prank.Prank;
import ch.heigvd.prank.PrankManager;
import ch.heigvd.smtp.SmtpClient;

import java.util.List;

public class SpamiBot {

    public static void main(String... args) {
        ConfigurationManager confman = new ConfigurationManager();
        PrankManager pman = new PrankManager(confman);
        List<Prank> pranks = pman.generatePranks();

        SmtpClient smtpClient = new SmtpClient(confman.getSmtpServerAddress(), confman.getSmtpServerPort());
        for (Prank p: pranks) {
            smtpClient.sendMail(p.getMail());
        }
    }
}
