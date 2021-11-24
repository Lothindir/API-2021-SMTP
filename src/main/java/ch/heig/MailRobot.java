package ch.heig;

import ch.heig.config.ConfigurationManager;
import ch.heig.prank.PrankManager;
import ch.heig.smtp.SmtpClient;

public class MailRobot {

    public static void main(String... args) {
        ConfigurationManager confman = new ConfigurationManager();
        PrankManager pman = new PrankManager(confman);



        SmtpClient smtpClient = new SmtpClient(confman.getHost(), confman.getPort());
        //smtpClient.sendMail();





    }




    //SmtpClient smcli = new SmtpClient()


}
