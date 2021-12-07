package ch.heigvd.mail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Implementation of a person. It just stores an e-mail address retrieved from
 * the victims.utf8 file.
 *
 * @author Anthony Coke
 * @author Francesco Monti
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Person {
   private final String emailAddress;

   /**
    * Copy constructor

    * @param other other person to copy from
    */
   public Person(Person other) {
      this.emailAddress = other.emailAddress;
   }

}
