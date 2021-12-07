package ch.heigvd.smtp;

import ch.heigvd.mail.Mail;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the SMTP client. Its goal is to communicate with an SMTP server and send the pranks generated to
 * each victim.
 *
 * @author Anthony Coke
 * @author Francesco Monti
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

        // try with ressource
        try (Socket clientSocket = new Socket(smtpServerAddress, smtpServerPort);
             BufferedReader br = new BufferedReader(
                     new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
             BufferedWriter bw = new BufferedWriter(
                     new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8))){

            String OK_CODE = "250 ";

            LOG.log(Level.INFO, "Connected to" + clientSocket);

            String line = br.readLine();
            LOG.log(Level.INFO, line);

            // EHLO
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

            //  define sender
            bw.write("MAIL FROM: " + mail.getFrom() + lineReturn);
            bw.flush();
            line = br.readLine();
            LOG.log(Level.INFO, "MAILFROM " + line);

            if(!line.startsWith(OK_CODE)){
                throw new IOException("Error occurred" + line);
            }

            // define recipients
            for(String receptTo : mail.getTo()) {
                bw.write("RCPT TO: " + receptTo + lineReturn);
                bw.flush();
                line = br.readLine();
                LOG.log(Level.INFO, "RCPT TO " + line);
                if(!line.startsWith(OK_CODE)){
                    throw new IOException("Error occurred : " + line);
                }
            }

            // define recipients in carbon copy
            for(String receptCC : mail.getCc()) {
                bw.write("RCPT TO: " + receptCC + lineReturn);
                bw.flush();
                line = br.readLine();
                LOG.log(Level.INFO, line);
                if(!line.startsWith(OK_CODE)){
                    throw new IOException("Error occurred : " + line);
                }
            }

            // write message with encoding
            bw.write("DATA\r\n");
            bw.flush();
            br.readLine();

            bw.write(encoding);
            bw.write("From: " + mail.getFrom() + lineReturn);

            // list recipients
            bw.write("To: ");
            int mailGetToSize = mail.getTo().size();
            for(int i = 0; i < mailGetToSize; ++i) {
                if(i == mailGetToSize - 1){
                    bw.write(mail.getTo().get(i));
                } else {
                    bw.write(mail.getTo().get(i) + ",");
                }
            }
            bw.write(lineReturn);

            // list carbon copy people
            bw.write("Cc: ");
            int mailGetCcSize = mail.getCc().size();
            for(int i = 0; i < mailGetCcSize; ++i) {
                if(i == mailGetCcSize - 1){
                    bw.write(mail.getCc().get(i));
                } else {
                    bw.write(mail.getCc().get(i) + ",");
                }
            }
            bw.write(lineReturn);
            bw.flush();

            // add subject and body, confirm e-mail
            bw.write(mail.getSubject() + lineReturn + lineReturn);
            bw.write(mail.getBody());
            bw.write(lineReturn);
            bw.write(".");
            bw.write(lineReturn);
            bw.flush();

            line = br.readLine();
            LOG.log(Level.INFO, line);
            if(!line.startsWith(OK_CODE)){
                throw new IOException("Error occurred" + line);
            }

            // quit
            bw.write("QUIT" + lineReturn);
            bw.flush();

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.toString(), e);
        }
    }
}