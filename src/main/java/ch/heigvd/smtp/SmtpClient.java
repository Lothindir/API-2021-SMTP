/**
 * Authors  : Anthony Coke, Francesco Monti
 * Date     : 2021-11-28
 */
package ch.heigvd.smtp;

import ch.heigvd.mail.Mail;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SMTP Client implementation
 */
public class SmtpClient {

    private final static Logger LOG = Logger.getLogger(SmtpClient.class.getName());
    private final int smtpServerPort;
    private final String smtpServerAddress;
    private final String lineReturn = "\r\n";
    private String encoding = "Content-Type: text/plain; charset=utf-8" + lineReturn;

    /**
     * Constructor with parameters, generates an SmtpClient
     * @param smtpServerAddress the ip address of the server
     * @param smtpServerPort the port used to communicate with the server
     */
    public SmtpClient(String smtpServerAddress, int smtpServerPort){
        this.smtpServerAddress = smtpServerAddress;
         this.smtpServerPort = smtpServerPort;

    }

    /**
     * Sends an e-mail. Connects to the server and talks with it using the SMTP protocol.
     * @param mail the e-mail to send
     */
    public void sendMail(Mail mail) {

        try (Socket clientSocket = new Socket(smtpServerAddress, smtpServerPort);
             BufferedReader br = new BufferedReader(
                     new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
             BufferedWriter bw = new BufferedWriter(
                     new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));){

            String OK_CODE = "250 ";

            LOG.log(Level.INFO, "Connected to" + clientSocket);

            String line = br.readLine();
            LOG.log(Level.INFO, line);

            bw.write("EHLO SpamiBot" + lineReturn);
            bw.flush();
            line = br.readLine();
            LOG.log(Level.INFO, "EHLO " + line);

            //
            if(!line.startsWith("250-")){
                throw new IOException("Error after saying EHLO " + line);
            }

            // wait for line starting with 250-
            while(line.startsWith("250-")){
                line = br.readLine();
                LOG.log(Level.INFO, "WHILE " + line);
            }

            bw.write("MAIL FROM: " + mail.get_from() + lineReturn);
            bw.flush();
            line = br.readLine();
            LOG.log(Level.INFO, "MAILFROM " + line);

            if(!line.startsWith(OK_CODE)){
                throw new IOException("Error occurred" + line);
            }

            for(String receptTo : mail.get_to()) {
                bw.write("RCPT TO: " + receptTo + lineReturn);
                bw.flush();
                line = br.readLine();
                LOG.log(Level.INFO, "RCPT TO " + line);
                if(!line.startsWith(OK_CODE)){
                    throw new IOException("Error occurred : " + line);
                }
            }

            for(String receptCC : mail.get_cc()) {
                bw.write("RCPT TO: " + receptCC + lineReturn);
                bw.flush();
                line = br.readLine();
                LOG.log(Level.INFO, line);
                if(!line.startsWith(OK_CODE)){
                    throw new IOException("Error occurred : " + line);
                }
            }

            bw.write("DATA\r\n");
            bw.flush();
            br.readLine();


            //

            bw.write(encoding);
            bw.write("From: " + mail.get_from() + lineReturn);


            bw.write("To: ");
            int mailGetToSize = mail.get_to().size();
            for(int i = 0; i < mailGetToSize; ++i) {
                if(i == mailGetToSize - 1){
                    bw.write(mail.get_to().get(i));
                } else {
                    bw.write(mail.get_to().get(i) + ",");
                }
            }
            bw.write(lineReturn);

            bw.write("Cc: ");
            int mailGetCcSize = mail.get_cc().size();
            for(int i = 0; i < mailGetCcSize; ++i) {
                if(i == mailGetCcSize - 1){
                    bw.write(mail.get_cc().get(i));
                } else {
                    bw.write(mail.get_cc().get(i) + ",");
                }
            }
            bw.write(lineReturn);
            bw.flush();

            bw.write(mail.get_subject() + lineReturn + lineReturn);
            bw.write(mail.get_body());
            bw.write(lineReturn);
            bw.write(".");
            bw.write(lineReturn);
            bw.flush();

            line = br.readLine();
            LOG.log(Level.INFO, line);
            if(!line.startsWith(OK_CODE)){
                throw new IOException("Error occurred" + line);
            }

            bw.write("QUIT" + lineReturn);
            bw.flush();

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.toString(), e);
        }
    }
}