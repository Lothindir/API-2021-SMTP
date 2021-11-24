package ch.heigvd.api.smtp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SMTP client implementation
 */
public class Client {
  private static final Logger LOG = Logger.getLogger(Client.class.getName());
  private static final String HOST = "localhost";
  private static final int PORT = 25;

  /**
   * Main function to run client
   *
   * @param args no args required
   */
  public static void main(String[] args) {
    // Log output on a single line
    System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

    try (Socket clientSocket = new Socket(HOST, PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {

      String line;
      while (in.ready() && (line = in.readLine()) != null) {
        LOG.info(line);
      }

      LOG.info("Sending EHLO...");
      out.write("EHLO smtp\n");
      out.flush();

      // while ((line = in.readLine()) != null) {
      // LOG.info(line);
      // }

    } catch (Exception e) {
      LOG.log(Level.SEVERE, e.toString(), e);
    }
  }
}
