package ch.heigvd.prank;

import ch.heigvd.config.ConfigurationManager;
import ch.heigvd.mail.Group;
import ch.heigvd.mail.Person;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class PrankManager {

    private final ConfigurationManager confMan;


    public PrankManager(ConfigurationManager configurationManager) {
        confMan = configurationManager;
    }


    public ArrayList<Prank> generatePranks() {
        ArrayList<Prank> pranks = new ArrayList<>();

        ArrayList<String> messages = confMan.getMessages();

        ArrayList<Person> victims = confMan.getVictims();

        //if(victims / confMan.)


        Collections.shuffle(victims);




        return pranks;
    }

    private ArrayList<Group> generateGroups() {
        ArrayList<Group> groups = new ArrayList<>();


        return groups;
    }

}
