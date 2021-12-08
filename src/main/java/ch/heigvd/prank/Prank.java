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
   private Person sender;

   @Getter
   @Setter
   private String message;

   private Group victims;
   private Group cc;

   /**
    * Constructs a Prank with the given sender, victims, cc and message
    * 
    * @param sender  the mail sender
    * @param victims the list of victims
    * @param cc      the list of people to cc
    * @param message the message to send
    */
   public Prank(Person sender, Group victims, Group cc, String message) {
      this.sender = new Person(sender);
      this.victims = new Group(victims);
      this.cc = new Group(cc);
      this.message = message;
   }

   /**
    * Gets the list of cc
    * 
    * @return the list of people to cc
    */
   public Group getCc() {
      return new Group(cc);
   }

   /**
    * Sets the chosen cc list
    * 
    * @param cc the list of people to cc
    */
   public void setCc(Group cc) {
      this.cc = new Group(cc);
   }

   /**
    * Gets the list of victims
    * 
    * @return the list of victims
    */
   public Group getVictims() {
      return new Group(victims);
   }

   /**
    * Sets the list of victims
    * 
    * @param victims
    */
   public void setVictims(Group victims) {
      this.victims = new Group(victims);
   }

   /**
    * Generates a mail to send with the stored sender, victims, cc and message.
    * 
    * @return the mail to send
    */
   public Mail getMail() {
      Mail m = new Mail(message);
      for (Person p : victims) {
         m.addTo(p);
      }

      for (Person p : cc) {
         m.addCc(p);
      }

      m.setFrom(sender.getEmailAddress());

      return m;
   }
}
