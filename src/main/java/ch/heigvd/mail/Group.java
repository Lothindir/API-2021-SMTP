package ch.heigvd.mail;

import java.util.ArrayList;

public class Group {
    ArrayList<Person> people = new ArrayList<>();

    /**
     *
     * @param person
     */
    public void addPerson(Person person) {
        people.add(person);
    }

    public ArrayList<Person> getPeople(){
        return new ArrayList<>(people);
    }

}
