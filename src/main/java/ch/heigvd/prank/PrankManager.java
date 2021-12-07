package ch.heigvd.prank;

import java.util.ArrayList;
import java.util.List;

import ch.heigvd.config.ConfigurationManager;
import ch.heigvd.mail.Group;
import ch.heigvd.mail.Person;

/**
 * Implementation of a SpamiBot. It will run the prank campaign.
 *
 * @author Anthony Coke
 * @author Francesco Monti
 */
public class PrankManager {

   // REMOVE ?
   private final ConfigurationManager confMan;
   // REDUNDANT ?
   private final int nbOfGroups;
   private final int nbOfVictims;

   /**
    * Constructor with parameters. It needs a configuration manager in order to
    * load the config files.
    * 
    * @param configurationManager a ConfigurationManager instance.
    */
   public PrankManager(ConfigurationManager configurationManager) {
      confMan = configurationManager;
      nbOfGroups = confMan.getNbOfGroups();
      nbOfVictims = confMan.getVictims().size();
   }

   /**
    * Generate an ArrayList of pranks
    * 
    * @return a List of {@link Prank} (can contain only one)
    */
   public List<Prank> generatePranks() {
      List<Prank> pranks = new ArrayList<>();
      List<String> messages = confMan.getMessages();
      Group cc = confMan.getPeopleToCc();
      List<Group> groups = generateGroups(confMan.getVictims(), nbOfGroups);

      int index = 0;
      for (Group g : groups) {
         // used to make sure the victims are randomly selected
         g.shuffle();

         // select a victim and set it as the sender
         Person sender = g.remove(g.size() - 1); // Remove last to avoid shifting all the list

         // generate the prank and add it to the pranks list
         Prank prank = new Prank(sender, g, cc, messages.get(index));
         index = (++index) % messages.size();

         pranks.add(prank);
      }

      return pranks;
   }

   /**
    * Generates a list of groups.
    * 
    * @param victims    the number of victims in the victims.utf8 file.
    * @param nbOfGroups the number of groups wished.
    * @return an List of {@link Group}.
    */
   private List<Group> generateGroups(Group victims, int nbOfGroups) throws ArithmeticException {
      List<Group> groups = new ArrayList<>();

      for (int i = 0; i < nbOfGroups; ++i) {
         Group group = new Group();
         groups.add(group);
      }

      Group newVictims = new Group(victims);

      // members randomly selected
      newVictims.shuffle();

      int nbPerGroup = nbOfVictims / nbOfGroups; // Can throw an ArithmeticException
      int startIndex = 0;

      for (int i = 0; i < nbOfGroups; ++i) {
         // a group is a sublist of all the victims. that last group may have more people
         // that the other groups.
         List<Person> subList;
         if (i != nbOfGroups - 1)
            subList = newVictims.getMembers().subList(startIndex, startIndex + nbPerGroup);
         else
            subList = newVictims.getMembers().subList(startIndex, newVictims.size());
         startIndex += nbPerGroup;
         groups.get(i).setMembers(subList);
      }

      return groups;
   }

}
