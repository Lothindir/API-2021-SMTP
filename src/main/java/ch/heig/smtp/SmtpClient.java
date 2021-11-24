package ch.heig.smtp;

import ch.heig.mail.Mail;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SmtpClient {

    private final static Logger LOG = Logger.getLogger(SmtpClient.class.getName());
    private int port;
    private String host = "localhost";

    public SmtpClient(String host, int port){
        this.host = host;
         this.port = port;

    }

    public void sendMail(Mail mail) {

        Socket clientSocket = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        BufferedReader usrin = null;


        try {
            clientSocket = new Socket(host, port);

            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));
            usrin = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            LOG.log(Level.INFO, "Connected to" + clientSocket);




        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.toString(), e);
        } finally {
            try {
                if (usrin != null)
                    usrin.close();
            } catch (IOException e) {
                LOG.log(Level.SEVERE, e.toString(), e);
            }
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                LOG.log(Level.SEVERE, e.toString(), e);
            }
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
                LOG.log(Level.SEVERE, e.toString(), e);
            }
            try {
                if (clientSocket != null && !clientSocket.isClosed())
                    clientSocket.close();
            } catch (IOException e) {
                LOG.log(Level.SEVERE, e.toString(), e);
            }
        }
    }
}