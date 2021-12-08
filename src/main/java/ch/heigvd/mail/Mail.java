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
   private final List<String> cc;
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
      cc = new ArrayList<>();
   }

   /**
    * Retrieves the subject and the body of the message to be sent.
    * 
    * @param message
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

   public List<String> getTo() {
      return new ArrayList<>(to);
   }

   public List<String> getCc() {
      return new ArrayList<>(cc);
   }

   public void addTo(Person p) {
      to.add(p.getEmailAddress());
   }

   public void addCc(Person p) {
      cc.add(p.getEmailAddress());
   }
}
