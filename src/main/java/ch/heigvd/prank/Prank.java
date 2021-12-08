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
   private Group bcc;

   /**
    * Constructs a Prank with the given sender, victims, blind carbon copy list and message
    * 
    * @param sender  the mail sender
    * @param victims the list of victims
    * @param bcc      the blind carbon copy list
    * @param message the message to send
    */
   public Prank(Person sender, Group victims, Group bcc, String message) {
      this.sender = new Person(sender);
      this.victims = new Group(victims);
      this.bcc = new Group(bcc);
      this.message = message;
   }

   /**
    * Gets the blind carbon copy list
    * 
    * @return the blind carbon copy list
    */
   public Group getBcc() {
      return new Group(bcc);
   }

   /**
    * Sets the chosen blind carbon copy list
    * 
    * @param bcc the blind carbon copy list
    */
   public void setBcc(Group bcc) {
      this.bcc = new Group(bcc);
   }

   /**
<<<<<<< HEAD
    * Gets the list of victims.
    * @return the list of victims.
=======
    * Gets the list of victims
    * 
    * @return the list of victims
>>>>>>> cb458804a36213adfc9e4979b5c38cee533c690f
    */
   public Group getVictims() {
      return new Group(victims);
   }

   /**
<<<<<<< HEAD
    * Sets the list of victims.
    * @param victims a group of victims.
=======
    * Sets the list of victims
    * 
    * @param victims the list of victims
    */
   public void setVictims(Group victims) {
      this.victims = new Group(victims);
   }

   /**
    * Generates a mail to send with the stored sender, victims, blind carbon copy list and message.
    * 
    * @return the mail to send
>>>>>>> cb458804a36213adfc9e4979b5c38cee533c690f
    */
   public Mail getMail() {
      Mail m = new Mail(message);
      for (Person p : victims) {
         m.addTo(p);
      }

      for (Person p : bcc) {
         m.addBcc(p);
      }

      m.setFrom(sender.getEmailAddress());

      return m;
   }
}
