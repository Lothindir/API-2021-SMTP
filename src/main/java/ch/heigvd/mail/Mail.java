package ch.heigvd.mail;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Implementation of a Mail. It will run the prank campaign.
 *
 * @author Anthony Coke
 * @author Francesco Monti
 */
@Getter
public class Mail {
   @Setter
   private String from;
   private final List<String> to;
   private final List<String> bcc;
   private String subject;
   private String body;

   /**
    * Constructor with parameters, it calls the parseMessage.
    * 
    * @param message the message with its subject and body.
    */
   public Mail(String message) {
      parseMessage(message);
      to = new ArrayList<>();
      bcc = new ArrayList<>();
   }

   /**
    * Retrieves the subject and the body of the message to be sent.
    * 
    * @param message the message to parse.
    */
   private void parseMessage(String message) {
      String[] strs = message.split("\n");
      if (strs[0].contains("Subject:")) {
         subject = strs[0].replaceFirst("Subject:", "");
      }
      StringBuilder sb = new StringBuilder();
      for (int i = 1; i < strs.length; ++i) {
         sb.append(strs[i]).append("\n");
      }
      body = sb.toString();
   }

   /**
    * Gets the list of the e-mail address in the recipient list.
    * @return the list of carbon copy.
    */
   public List<String> getTo() {
      return new ArrayList<>(to);
   }

   /**
    * Gets the list of the e-mail address in the blind carbon copy list.
    * @return the list of blind carbon copy.
    */
   public List<String> getBcc() {
      return new ArrayList<>(bcc);
   }

   /**
    * Add a person's email address to the recipient list.
    * @param p the person to add.
    */
   public void addTo(Person p) {
      to.add(p.getEmailAddress());
   }

   /**
    * Add a person's e-mail address to the blind carbon copy list.
    * @param p the person to add.
    */
   public void addBcc(Person p) {
      bcc.add(p.getEmailAddress());
   }
}
