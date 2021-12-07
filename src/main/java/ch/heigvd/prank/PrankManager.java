package ch.heigvd.prank;

import ch.heigvd.config.ConfigurationManager;
import ch.heigvd.mail.Group;
import ch.heigvd.mail.Mail;
import ch.heigvd.mail.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of a SpamiBot. It will run the prank campaign.
 *
 * @author Anthony Coke
 * @author Francesco Monti
 */
public class PrankManager {

    // REMOVE ?
    private final static Logger LOG = Logger.getLogger(PrankManager.class.getName());
    private final ConfigurationManager confMan;
    // REDUNDANT ?
    private final int nbOfGroups;
    private final int nbOfVictims;


    /**
     * Constructor with parameters. It needs a configuration manager in order to load the config files.
     * @param configurationManager a ConfigurationManager instance.
     */
    public PrankManager(ConfigurationManager configurationManager) {
        confMan = configurationManager;
        nbOfGroups = confMan.getNbOfGroups();
        nbOfVictims = confMan.getVictims().size();
    }

    /**
     * Generate an ArrayList of pranks
     * @return a List<> of pranks (can contain only one)
     */
    public ArrayList<Prank> generatePranks() {
        ArrayList<Prank> pranks = new ArrayList<>();

        ArrayList<String> messages = confMan.getMessages();
        ArrayList<Person> listPersonCC = confMan.getPeopleToCc();

        ArrayList<Group> groups = generateGroups(confMan.getVictims(), nbOfGroups);
        int index = 0;
        for(Group g : groups){

            // get the victims
            ArrayList<Person> victims = g.getMembers();

            // used to make sure the victims are randomly selected
            Collections.shuffle(victims);

            // select a message
            Mail mail = new Mail(messages.get(index));
            index = (++index) % messages.size(); // to loop on the message list

            // remove a victim and set it as sender
            Person sender = victims.remove(0);
            mail.setFrom(sender.getEmailAddress());

            ArrayList<String> listTo = new ArrayList<>();

            // the remaining victims are added to the recipient list
            for(Person p: victims) {
                listTo.add(p.getEmailAddress());
            }
            mail.setTo(listTo);

            ArrayList<String> listStringCC = new ArrayList<>();

            // set the list of carbon copy people
            for(Person p : listPersonCC) {
                listStringCC.add(p.getEmailAddress());
            }
            mail.setCc(listStringCC);

            // generate the prank
            Prank prank = new Prank(sender, victims, confMan.getPeopleToCc(), mail, g);
            pranks.add(prank);
        }

        return pranks;
    }

    /**
     * Generates a list of groups.
     * @param victims the number of victims in the victims.utf8 file.
     * @param nbOfGroups the number of groups wished.
     * @return an ArrayList<> of groups.
     */
    private ArrayList<Group> generateGroups(ArrayList<Person> victims, int nbOfGroups) {
        ArrayList<Group> groups = new ArrayList<>();

        for(int i = 0; i < nbOfGroups; ++i) {
            Group group = new Group();
            groups.add(group);
        }

        ArrayList<Person> newVictims = new ArrayList<>(victims);

        // members randomly selected
        Collections.shuffle(newVictims);

        int nbPerGroup = nbOfVictims / nbOfGroups;
        int startIndex = 0;

        for(int i = 0; i < nbOfGroups; ++i) {

            // a group is a sublist of all the victims. that last group may have more people that the other groups.
            List<Person> subList;
            if(i != nbOfGroups - 1)
                subList = newVictims.subList(startIndex, startIndex + nbPerGroup);
            else
                subList = newVictims.subList(startIndex, newVictims.size());
            startIndex += nbPerGroup;
            groups.get(i).setMembers(subList);
        }

        return groups;
    }

}
