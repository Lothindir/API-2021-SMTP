/**
 * Authors  : Anthony Coke, Francesco Monti
 * Date     : 2021-11-28
 */
package ch.heigvd.mail;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
/**
 * Implementation of a Person
 */
public class Person {
    private final String _emailAddress;
    //private final String _firstName;
    //private final String _lastName;

    /**
     * Constructor with parameters
     * @param emailAddress the email address of the person to be created
     */
    public Person(String emailAddress) {
        _emailAddress = emailAddress;
    }

}
