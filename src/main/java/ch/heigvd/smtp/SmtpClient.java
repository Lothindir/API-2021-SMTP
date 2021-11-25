package ch.heigvd.smtp;

import ch.heigvd.mail.Mail;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SmtpClient {

    private final static Logger LOG = Logger.getLogger(SmtpClient.class.getName());
    private final int smtpServerPort;
    private final String smtpServerAddress;
    private final String lineReturn = "\r\n";
    private String encoding = "Content-Type: text/plain; charset=utf-8" + lineReturn;

    public SmtpClient(String smtpServerAddress, int smtpServerPort){
        this.smtpServerAddress = smtpServerAddress;
         this.smtpServerPort = smtpServerPort;

    }

    public void sendMail(Mail mail) {

        try (Socket clientSocket = new Socket(smtpServerAddress, smtpServerPort);
             BufferedReader br = new BufferedReader(
                     new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
             BufferedWriter bw = new BufferedWriter(
                     new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));){

            LOG.log(Level.INFO, "Connected to" + clientSocket);

            //


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

            bw.write("MAIL FROM: " + mail.getFrom() + lineReturn);
            bw.flush();
            line = br.readLine();
            LOG.log(Level.INFO, "MAILFROM " + line);

            if(!line.startsWith("250 ")){
                throw new IOException("Error occurred" + line);
            }

            for(String receptTo : mail.getTo()) {
                bw.write("RCPT TO: " + receptTo + lineReturn);
                bw.flush();
                line = br.readLine();
                LOG.log(Level.INFO, "RCPT TO " + line);
                if(!line.startsWith("250 ")){
                    throw new IOException("Error occurred : " + line);
                }
            }

            for(String receptCC : mail.getCc()) {
                bw.write("RCPT TO: " + receptCC + lineReturn);
                bw.flush();
                line = br.readLine();
                LOG.log(Level.INFO, line);
                if(!line.startsWith("250 ")){
                    throw new IOException("Error occurred : " + line);
                }
            }

            bw.write("DATA\r\n");
            bw.flush();
            br.readLine();


            //

            bw.write(encoding);
            bw.write("From: " + mail.getFrom() + lineReturn);


            bw.write("To: ");
            for(int i = 0; i < mail.getTo().size(); ++i) {
                if(i == mail.getTo().size() - 1){
                    bw.write(mail.getTo().get(i));
                } else {
                    bw.write(mail.getTo().get(i) + ",");
                }
            }
            bw.write(lineReturn);

            bw.write("Cc: ");
            for(int i = 0; i < mail.getCc().size(); ++i) {
                if(i == mail.getTo().size() - 1){
                    bw.write(mail.getCc().get(i));
                } else {
                    bw.write(mail.getCc().get(i) + ",");
                }
            }
            bw.write(lineReturn);
            bw.flush();

            bw.write(mail.getSubject() + lineReturn + lineReturn);
            bw.write(mail.getBody());
            bw.write(lineReturn);
            bw.write(".");
            bw.write(lineReturn);
            bw.flush();

            line = br.readLine();
            LOG.log(Level.INFO, line);
            if(!line.startsWith("250 ")){
                throw new IOException("Error occurred" + line);
            }

            bw.write("QUIT" + lineReturn);
            bw.flush();

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.toString(), e);
        }
    }
}