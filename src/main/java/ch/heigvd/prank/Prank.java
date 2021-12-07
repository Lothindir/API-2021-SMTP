package ch.heigvd.prank;

import ch.heigvd.mail.Group;
import ch.heigvd.mail.Mail;
import ch.heigvd.mail.Person;
import lombok.Getter;
import lombok.Setter;

/**
 * Implementation of a prank.
 *
 * @author Anthony Coke
 * @author Francesco Monti
 */
public class Prank {
   @Getter
   @Setter
   private Person chosenSender;

   @Getter
   @Setter
   private String message;

   private Group chosenVictims;
   private Group chosenCC;


   public Prank(Person sender, Group victims, Group cc, String message) {
      this.chosenSender = new Person(sender);
      this.chosenVictims = new Group(victims);
      this.chosenCC = new Group(cc);
      this.message = message;
   }

   public Group getChosenCC() {
      return new Group(chosenCC);
   }

   public void setChosenCC(Group chosenCC) {
      this.chosenCC = new Group(chosenCC);
   }

   public Group getChosenVictims() {
      return new Group(chosenVictims);
   }

   public void setChosenVictims(Group chosenVictims) {
      this.chosenVictims = new Group(chosenVictims);
   }

   public Mail getMail() {
      Mail m = new Mail(message);
      for (Person p : chosenVictims) {
         m.addTo(p);
      }

      for (Person p : chosenCC) {
         m.addCc(p);
      }

      m.setFrom(chosenSender.getEmailAddress());

      return m;
   }
}
