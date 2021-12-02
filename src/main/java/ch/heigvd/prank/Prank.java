package ch.heigvd.prank;

import ch.heigvd.mail.Group;
import ch.heigvd.mail.Mail;
import ch.heigvd.mail.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Implementation of a prank.
 *
 * @author Anthony Coke
 * @author Francesco Monti
 */
@Getter @Setter @AllArgsConstructor
public class Prank {
    @NonNull private Person chosenSender;
    @NonNull private ArrayList<Person> chosenVictims;
    @NonNull private ArrayList<Person> chosenCC;
    @NonNull private final Mail mail;
    @NonNull private final Group group;

    /**
     * Retrieves the list of the victims of the prank.
     * @return a copy of the list of Person.
     */
    public ArrayList<Person> getChosenVictims() {
        return new ArrayList<>(chosenVictims);
    }

    /**
     * Retrieves the list of the people to add as carbon copy.
     * @return a copy of the list of Person.
     */
    public ArrayList<Person> getChosenCC() {
        return new ArrayList<>(chosenCC);
    }
}

