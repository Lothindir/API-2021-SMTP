package ch.heigvd.smtp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.heigvd.mail.Mail;

/**
 * Implementation of the SMTP client. Its goal is to communicate with an SMTP
 * server and send the pranks generated to
 * each victim.
 *
 * @author Anthony Coke
 * @author Francesco Monti
 */
public class SmtpClient implements AutoCloseable {

   private final static Logger LOG = Logger.getLogger(SmtpClient.class.getName());
   private final static int MAX_RETRIES = 5;

   private final int smtpServerPort;
   private final String smtpServerAddress;
   private final String lineReturn = "\r\n";
   private final String encoding = "Content-Type: text/plain; charset=utf-8" + lineReturn;
   private final String smtpUser;
   private final String smtpPassword;

   private Socket clientSocket;
   private BufferedReader br;
   private BufferedWriter bw;

   /**
    * Constructor with parameters, generates an SmtpClient
    * 
    * @param smtpServerAddress the ip address of the server
    * @param smtpServerPort    the port used to communicate with the server
    * @throws UnknownHostException if the smtp server host is unknown
    * @throws IOException          if an I/O error occurs when connecting to the
    *                              server
    */
   public SmtpClient(String smtpServerAddress, int smtpServerPort) throws UnknownHostException, IOException {
      this(smtpServerAddress, smtpServerPort, "", "");
   }

   /**
    * Constructor with parameters, generates an SmtpClient with auth
    * 
    * @param smtpServerAddress the ip address of the server
    * @param smtpServerPort    the port used to communicate with the server
    * @param smtpUser          the user for the smtp auth
    * @param smtpPassword      the plain password for the smtp auth
    * @throws UnknownHostException if the smtp server host is unknown
    * @throws IOException          if an I/O error occurs when connecting to the
    *                              server
    */
   public SmtpClient(String smtpServerAddress, int smtpServerPort, String smtpUser, String smtpPassword)
         throws UnknownHostException, IOException {
      this.smtpServerAddress = smtpServerAddress;
      this.smtpServerPort = smtpServerPort;
      this.smtpUser = smtpUser;
      this.smtpPassword = smtpPassword;

      connect();
   }

   /**
    * Connects to the SMTP server and initialises all associated resources
    * 
    * @throws UnknownHostException if the smtp server host is unknown
    * @throws IOException          if an I/O error occurs when connecting to the
    *                              server
    */
   public void connect() throws UnknownHostException, IOException {
      clientSocket = new Socket(smtpServerAddress, smtpServerPort);
      br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
      bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));

      boolean shouldAuthenticate = false;

      LOG.info("Connected to " + clientSocket);

      String line = br.readLine();
      LOG.info(line);

      // EHLO
      bw.write("EHLO SpamiBot" + lineReturn);
      bw.flush();
      line = br.readLine();
      LOG.info("EHLO " + line);

      if (!line.startsWith("250-")) {
         throw new IOException("Error after saying EHLO " + line);
      }

      // wait for line starting with 250-
      while (line.startsWith("250-")) {
         line = br.readLine();
         if (line.contains("AUTH"))
            shouldAuthenticate = true;
         LOG.info(line);
      }

      // authenticate if needed
      if (shouldAuthenticate) {
         if (smtpUser == "" || smtpPassword == "") {
            throw new RuntimeException(
                  "No user or password defined, authentication is required by the server. Please define credentials in config file (smtpUser and smtpPassword).");
         }
         bw.write("AUTH LOGIN" + lineReturn);
         bw.flush();
         line = br.readLine();
         if (!line.equals("334 VXNlcm5hbWU6")) {
            throw new IOException("Error occurred: " + line);
         }
         LOG.info(line);
         bw.write(encodeBase64(this.smtpUser) + lineReturn);
         bw.flush();
         line = br.readLine();
         if (!line.equals("334 UGFzc3dvcmQ6")) {
            throw new IOException("Error occurred: " + line);
         }
         LOG.info(line);
         bw.write(encodeBase64(smtpPassword) + lineReturn);
         bw.flush();
         line = br.readLine();
         if (!line.startsWith("235")) {
            throw new IOException("Error occurred: " + line);
         }
         LOG.info("AUTH Successful");
      }
      LOG.info(lineReturn);
   }

   /**
    * Sends an e-mail. Connects to the server and talks with it using the SMTP
    * protocol.
    * 
    * @param mail the e-mail to send
    */
   public void sendMail(Mail mail) throws IOException {
      int retries = 0;
      // if the client is unavailable try to reconnect
      while (clientSocket == null /* || clientSocket.isClosed() */) {
         connect();
         if (retries++ > MAX_RETRIES) {
            LOG.severe("Cannot connect to the SMTP server");
            return;
         }
      }
      String readline, writeline;
      String OK_CODE = "250 ";

      LOG.info("Sending mail...");

      // define sender
      writeline = "MAIL FROM: <" + mail.getFrom() + ">";
      bw.write(writeline + lineReturn);
      bw.flush();
      readline = br.readLine();
      if (!readline.startsWith(OK_CODE)) {
         LOG.severe(writeline + " => " + readline);
         throw new IOException("Error occurred: " + readline);
      }
      LOG.info(writeline + " => " + readline);

      // define recipients
      for (String receptTo : mail.getTo()) {
         writeline = "RCPT TO: <" + receptTo + ">";
         bw.write(writeline + lineReturn);
         bw.flush();
         readline = br.readLine();
         if (!readline.startsWith(OK_CODE)) {
            LOG.severe(writeline + " => " + readline);
            throw new IOException("Error occurred: " + readline);
         }
         LOG.info(writeline + " => " + readline);
      }

      // define recipients in carbon copy
      for (String receptCC : mail.getCc()) {
         bw.write("RCPT TO: <" + receptCC + ">" + lineReturn);
         bw.flush();
         readline = br.readLine();
         if (!readline.startsWith(OK_CODE)) {
            LOG.severe("CC TO: " + receptCC + " => " + readline);
            throw new IOException("Error occurred: " + readline);
         }
         LOG.info("CC TO: " + receptCC + " => " + readline);
      }

      // write message with encoding
      bw.write("DATA\r\n");
      bw.flush();
      readline = br.readLine();
      LOG.info("Writing DATA...");

      bw.write(encoding);
      bw.write("From: " + mail.getFrom() + lineReturn);

      // list recipients
      bw.write("To: ");
      int mailGetToSize = mail.getTo().size();
      for (int i = 0; i < mailGetToSize; ++i) {
         if (i == mailGetToSize - 1) {
            bw.write(mail.getTo().get(i));
         } else {
            bw.write(mail.getTo().get(i) + ",");
         }
      }
      bw.write(lineReturn);

      // list carbon copy people
      bw.write("Cc: ");
      int mailGetCcSize = mail.getCc().size();
      for (int i = 0; i < mailGetCcSize; ++i) {
         if (i == mailGetCcSize - 1) {
            bw.write(mail.getCc().get(i));
         } else {
            bw.write(mail.getCc().get(i) + ",");
         }
      }
      bw.write(lineReturn);
      bw.flush();

      // add subject(b64 encoded for utf-8 header) and body, confirm e-mail
      bw.write("Subject: =?utf-8?B?" + encodeBase64(mail.getSubject()) + "?=" + lineReturn + lineReturn);
      bw.write(mail.getBody());
      bw.write(".");
      bw.write(lineReturn);
      bw.flush();

      readline = br.readLine();
      LOG.info(readline + lineReturn);
      if (!readline.startsWith(OK_CODE)) {
         throw new IOException("Error occurred: " + readline);
      }
   }

   /**
    * Closes the client and all associated resources
    */
   public void close() {
      try {
         bw.write("QUIT" + lineReturn);
         bw.flush();
         LOG.info("Quitting...");
      } catch (IOException e) {
         LOG.log(Level.SEVERE, e.getMessage(), e);
      } finally {
         try {
            if (br != null)
               br.close();

            if (bw != null)
               bw.close();

            if (clientSocket != null && !clientSocket.isClosed())
               clientSocket.close();
         } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
         }
      }
   }

   /**
    * Encode a string in Base64
    * 
    * @param str the string to be encoded
    * @return the base64 encoded string
    */
   private String encodeBase64(String str) {
      return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
   }
}