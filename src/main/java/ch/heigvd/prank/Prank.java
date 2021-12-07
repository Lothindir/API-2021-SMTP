package ch.heigvd.prank;

import ch.heigvd.mail.Group;
import ch.heigvd.mail.Mail;
import ch.heigvd.mail.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;

/**
 * Implementation of a prank.
 *
 * @author Anthony Coke
 * @author Francesco Monti
 */
@AllArgsConstructor
public class Prank {
    @NonNull private Person chosenSender;
    @NonNull private ArrayList<Person> chosenVictims;
    @NonNull private ArrayList<Person> chosenCC;
    @NonNull private final Mail mail;
    @NonNull private final Group group;

    // TODO : DELETE CES GETTERS ?

    public Mail getMail() {
        return mail;
    }
}

