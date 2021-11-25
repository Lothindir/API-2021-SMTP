package ch.heigvd.config;

import ch.heigvd.mail.Person;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Setter
public class ConfigurationManager {
    private final static Logger LOG = Logger.getLogger(ConfigurationManager.class.getName());

    /**
     * Attributs
     */
    private String smtpServerAddress;
    private int smtpServerPort;
    private ArrayList<Person> victims;
    private ArrayList<String> messages;
    private int nbOfGroups;
    private ArrayList<Person> personToCC;

    public ConfigurationManager() {
        this("");
    }

    public ConfigurationManager(String path) {
        if (!path.isEmpty() && !(path.endsWith("/") || path.endsWith("\\"))) {
            if (System.getProperty("os.name").toLowerCase().contains("win"))
                path += "\\";
            else
                path += "/";
        }

        victims = getVictimsFromFile(path + "victims.utf8");
        messages = getMessagesFromFile(path + "messages.utf8");
        loadProperties(path + "config.properties");
    }

    public ArrayList<Person> getVictims() {
        return new ArrayList<>(victims);
    }

    public ArrayList<String> getMessages() {
        return new ArrayList<>(messages);
    }

    public ArrayList<Person> getPersonToCC() {
        return new ArrayList<>(personToCC);
    }

    /**
     *
     * @param path
     */
    public void loadProperties(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            Properties properties = new Properties();
            properties.load(fis);

            this.smtpServerAddress = properties.getProperty("smtpServerAddress");
            this.smtpServerPort = Integer.parseInt(properties.getProperty("smtpServerPort"));
            this.nbOfGroups = Integer.parseInt(properties.getProperty("numberOfGroups"));
            this.personToCC = new ArrayList<>();
            String str = properties.getProperty("personToCC");
            String[] tab = str.split(",");
            for (String s : tab) {
                this.personToCC.add(new Person(s));
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Cannot find file at: " + System.getProperty("user.dir") + "/" + path, e);
        }

    }

    /**
     *
     * @param path
     * @return
     */
    public ArrayList<Person> getVictimsFromFile(String path) {
        ArrayList<Person> list = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
            String line;

            while ((line = r.readLine()) != null) {
                list.add(new Person(line));
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Cannot find file at: " + System.getProperty("user.dir") + "/" + path, e);
        }

        return list;
    }

    /**
     *
     * @param path
     * @return
     */
    public ArrayList<String> getMessagesFromFile(String path) {
        ArrayList<String> list = new ArrayList<>();
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
            LOG.log(Level.SEVERE, "Cannot find file at: " + System.getProperty("user.dir") + "/" + path, e);
        }

        return list;
    }
}
