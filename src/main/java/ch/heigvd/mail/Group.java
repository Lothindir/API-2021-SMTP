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
    ArrayList<Person> people = new ArrayList<>();

    /**
     * Adds a person to a group (not used).
     * @param person the person to be added.
     */
    public void addPerson(Person person) {
        people.add(person);
    }

    /**
     * Retrieves the people in the group.
     * @return a list of Person that are part of the group.
     */
    public ArrayList<Person> getPeople(){
        return new ArrayList<>(people);
    }

    /**
     * Sets the people of the group.
     * @param newPeople a list of Person representing the group.
     */
    public void setPeople(List<Person> newPeople) {
        people = new ArrayList<>(newPeople);
    }
}
