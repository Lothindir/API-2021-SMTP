package ch.heigvd.config;

import ch.heigvd.mail.Person;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;

public class ConfigurationManager {
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
        victims = getVictimsFromFile("victims.utf8");
        messages = getMessagesFromFile("messages.utf8");
        loadProperties("config.properties");
    }



    public ArrayList<Person> getVictims() {
        return victims;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public String getHost() {
        return smtpServerAddress;
    }
    public int getPort() {
        return smtpServerPort;
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
            for(String s : tab) {
                this.personToCC.add(new Person(s));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**
     *
     * @param path
     * @return
     */
    public ArrayList<Person> getVictimsFromFile(String path) {
        ArrayList<Person> list = new ArrayList<>();
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8);
            BufferedReader r = new BufferedReader(isr);

            String line;

            while ((line = r.readLine()) != null) {
                list.add(new Person(line));
            }

        } catch (IOException e) {
            e.printStackTrace();
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
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8);
            BufferedReader r = new BufferedReader(isr);

            String line;
            StringBuilder sb = new StringBuilder();
            while((line = r.readLine()) != null) {
                if(line.equals("----------------")) {
                    list.add(sb.toString());
                    sb.setLength(0);
                    continue;
                }
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
