package ch.heigvd.mail;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a group.
 *
 * @author Anthony Coke
 * @author Francesco Monti
 */
public class Group {
    ArrayList<Person> members = new ArrayList<>();

    /**
     * Retrieves the people in the group.
     * @return a list of Person that are part of the group.
     */
    public ArrayList<Person> getMembers(){
        return new ArrayList<>(members);
    }

    /**
     * Sets the people of the group.
     * @param newPeople a list of Person representing the group.
     */
    public void setMembers(List<Person> newPeople) {
        members = new ArrayList<>(newPeople);
    }
}
