/**
 * Authors  : Anthony Coke, Francesco Monti
 * Date     : 2021-11-28
 */
package ch.heigvd.mail;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Group {
    ArrayList<Person> people = new ArrayList<>();

    /**
     *
     * @param person
     */
    public void addPerson(Person person) {
        people.add(person);
    }

    /**
     *
     * @return
     */
    public ArrayList<Person> getPeople(){
        return new ArrayList<>(people);
    }

    /**
     *
     * @param newPeople
     */
    public void setPeople(List<Person> newPeople) {
        people = new ArrayList<>(newPeople);
    }
}
