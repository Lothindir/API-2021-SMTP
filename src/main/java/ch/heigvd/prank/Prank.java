/**
 * Authors  : Anthony Coke, Francesco Monti
 * Date     : 2021-11-28
 */
package ch.heigvd.prank;

import ch.heigvd.mail.Group;
import ch.heigvd.mail.Mail;
import ch.heigvd.mail.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter @AllArgsConstructor
/**
 *
 */
public class Prank {
    @NonNull private Person chosenSender;
    @NonNull private ArrayList<Person> chosenVictims;
    @NonNull private ArrayList<Person> chosenCC;
    @NonNull private final Mail mail;
    @NonNull private final Group group;

    /**
     *
     * @return
     */
    public ArrayList<Person> getChosenVictims() {
        return new ArrayList<>(chosenVictims);
    }

    /**
     *
     * @return
     */
    public ArrayList<Person> getChosenCC() {
        return new ArrayList<>(chosenCC);
    }
}

