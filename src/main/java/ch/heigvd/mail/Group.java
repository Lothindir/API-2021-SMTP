package ch.heigvd.mail;

import java.util.ArrayList;
import java.util.List;

public class Group {
    ArrayList<Person> people = new ArrayList<>();

    public void addPerson(Person person) {
        people.add(person);
    }

    public ArrayList<Person> getPeople(){
        return new ArrayList<>(people);
    }

    public void setPeople(List<Person> newPeople) {
        people = new ArrayList<>(newPeople);
    }

}
