package ch.heigvd.config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.heigvd.mail.Group;
import ch.heigvd.mail.Person;
import lombok.Getter;

/**
 * Implementation of the configuration manager. Its main goal is to load
 * retrieve the data from all the files.
 *
 * @author Anthony Coke
 * @author Francesco Monti
 */
@Getter
public class ConfigurationManager {
   private final static Logger LOG = Logger.getLogger(ConfigurationManager.class.getName());

   /**
    * Attributs
    */
   private String smtpServerAddress;
   private int smtpServerPort;
   private String smtpUser;
   private String smtpPassword;
   private int nbOfGroups;
   private ArrayList<String> messages;
   private Group victims;
   private Group peopleToCc;

   /**
    * Default constructor.
    */
   public ConfigurationManager() {
      this("");
   }

   /**
    * Constructor with parameters, it loads the config from all the files.
    * 
    * @param path the path of the folder containing the config files.
    */
   public ConfigurationManager(String path) {
      /** Thanks Windows ! */
      if (!path.isEmpty() && !(path.endsWith("/") || path.endsWith("\\"))) {
         if (System.getProperty("os.name").toLowerCase().contains("win"))
            path += "\\";
         else
            path += "/";
      }

      victims = getVictimsFromFile(path + "victims.utf8");
      messages = getMessagesFromFile(path + "messages.utf8");
      loadProperties(path + "config.properties");

      // if there is not enough victims per group, we redefine the nbOfGroups to
      // assure there are 3 at least 3 people
      // per group (requirement of the lab).
      if (victims.size() / nbOfGroups < 3) {
         throw new RuntimeException("Not enough people per group ! You need at least 3 people in a group.");
      }
   }

   /**
    * Get the list of the people to prank.
    * 
    * @return a copy of the list to respect encapsulation.
    */
   public Group getVictims() {
      return new Group(victims);
   }

   /**
    * Get the list of the messages that can be sent.
    * 
    * @return a copy of the list of String to respect encapsulation.
    */
   public ArrayList<String> getMessages() {
      return new ArrayList<>(messages);
   }

   /**
    * Get the list of the people to add as carbon copy.
    * 
    * @return a copy of the list to respect encapsulation.
    */
   public Group getPeopleToCc() {
      return new Group(peopleToCc);
   }

   /**
    * Loads the properties from the config.properties file.
    * 
    * @param path the path of the file.
    */
   public void loadProperties(String path) {
      try {
         FileInputStream fis = new FileInputStream(path);
         Properties properties = new Properties();
         properties.load(fis);

         this.smtpServerAddress = properties.getProperty("smtpServerAddress");
         this.smtpServerPort = Integer.parseInt(properties.getProperty("smtpServerPort"));
         this.nbOfGroups = Integer.parseInt(properties.getProperty("numberOfGroups"));
         if (nbOfGroups <= 0)
            throw new RuntimeException("nbOfGroups cannot be equal or smaller that zero !");
         this.peopleToCc = new Group();

         // Retrieve each e-mail address seperated by a comma.
         String str = properties.getProperty("peopleToCC");
         String[] tab = str.split(",");
         for (String s : tab) {
            this.peopleToCc.add(new Person(s));
         }

         this.smtpUser = properties.getProperty("smtpUser", "");
         this.smtpPassword = properties.getProperty("smtpPassword", "");

      } catch (IOException e) {
         LOG.severe("Cannot find file at: " + System.getProperty("user.dir") + "/" + path);
         throw new RuntimeException(path + " not found");
      }

   }

   /**
    * Retrieves the victims from a file.
    * 
    * @param path the path of the file.
    * @return an ArrayList of Person.
    */
   public Group getVictimsFromFile(String path) {
      Group list = new Group();
      try (BufferedReader r = new BufferedReader(
            new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
         String line;

         while ((line = r.readLine()) != null) {
            list.add(new Person(line));
         }

      } catch (IOException e) {
         LOG.severe("Cannot find file at: " + System.getProperty("user.dir") + "/" + path);
         throw new RuntimeException(path + " not found");
      }

      return list;
   }

   /**
    * Retrieves the defined messages from a file.
    * 
    * @param path the path of the file containing the messages.
    * @return an ArrayList of String.
    */
   public ArrayList<String> getMessagesFromFile(String path) {
      ArrayList<String> list = new ArrayList<>();

      // try with ressource
      try (BufferedReader r = new BufferedReader(
            new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {

         String line;
         StringBuilder sb = new StringBuilder();
         while ((line = r.readLine()) != null) {
            if (line.equals("----------------")) {
               list.add(sb.toString());
               sb.setLength(0);
               continue;
            }
            sb.append(line).append("\r\n");
         }
      } catch (IOException e) {
         LOG.severe("Cannot find file at: " + System.getProperty("user.dir") + "/" + path);
         throw new RuntimeException(path + " not found");
      }

      return list;
   }
}
