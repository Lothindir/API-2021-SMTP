package ch.heigvd.mail;

import lombok.Getter;
import lombok.Setter;

/**
 * Implementation of a person. It just stores an e-mail address retrieved from the victims.utf8 file.
 *
 * @author Anthony Coke
 * @author Francesco Monti
 */
@Getter @Setter
public class Person {
    private final String _emailAddress;

    /**
     * Constructor with parameters.
     * @param emailAddress the email address of the person to be created.
     */
    public Person(String emailAddress) {
        _emailAddress = emailAddress;
    }

}
