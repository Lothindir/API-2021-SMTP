package ch.heigvd.prank;

import ch.heigvd.config.ConfigurationManager;
import ch.heigvd.mail.Group;
import ch.heigvd.mail.Mail;
import ch.heigvd.mail.Person;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Setter
public class PrankManager {

    private final static Logger LOG = Logger.getLogger(PrankManager.class.getName());
    private final ConfigurationManager confMan;
    private int nbOfGroups;
    private int nbOfVictims;


    public PrankManager(ConfigurationManager configurationManager) {
        confMan = configurationManager;
        nbOfGroups = confMan.getNbOfGroups();
        nbOfVictims = confMan.getVictims().size();
    }


    public ArrayList<Prank> generatePranks() {
        ArrayList<Prank> pranks = new ArrayList<>();

        ArrayList<String> messages = confMan.getMessages();
        ArrayList<Person> listPersonCC = confMan.getPersonToCC();

        if(nbOfVictims / nbOfGroups < 3)
        {
            LOG.log(Level.WARNING, "Not enough people per group ! You need at least 3 people in a group.");
            nbOfGroups = nbOfVictims / 3;
        }
        ArrayList<Group> groups = generateGroups(confMan.getVictims(), nbOfGroups);
        int index = 0;
        for(Group g : groups){
            ArrayList<Person> victims = g.getPeople();
            Collections.shuffle(victims);
            Mail mail = new Mail(messages.get(index));
            index = (++index) % messages.size(); // to loop on the message list
            Person sender = victims.remove(0);
            mail.setFrom(sender.getEmailAddress());
            ArrayList<String> listTo = new ArrayList<>();

            for(Person p: victims) {
                listTo.add(p.getEmailAddress());
            }

            mail.setTo(listTo);

            ArrayList<String> listStringCC = new ArrayList<>();
            for(Person p : listPersonCC) {
                listStringCC.add(p.getEmailAddress());
            }

            mail.setCc(listStringCC);
            Prank prank = new Prank(sender, victims, confMan.getPersonToCC(), mail, g);
            pranks.add(prank);
        }

        return pranks;
    }

    private ArrayList<Group> generateGroups(ArrayList<Person> victims, int nbOfGroups) {
        ArrayList<Group> groups = new ArrayList<>();
        for(int i = 0; i < nbOfGroups; ++i) {
            Group group = new Group();
            groups.add(group);
        }

        ArrayList<Person> newVictims = new ArrayList<>(victims);
        Collections.shuffle(newVictims);
        int nbPerGroup = nbOfVictims / nbOfGroups;
        int startIndex = 0;
        Group currentGroup;
        for(int i = 0; i < nbOfGroups; ++i) {
            currentGroup = groups.get(i);
            List<Person> subList;
            if(i != nbOfGroups - 1)
                subList = newVictims.subList(startIndex, startIndex + nbPerGroup);
            else
                subList = newVictims.subList(startIndex, newVictims.size());
            startIndex += nbPerGroup;
            currentGroup.setPeople(subList);
        }
        return groups;
    }

}
