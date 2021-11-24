package ch.heig.mail;

import java.util.ArrayList;

public class Group {
    ArrayList<Person> listPeople = new ArrayList<>();

    /**
     *
     * @param person
     */
    public void addPerson(Person person) {
        listPeople.add(person);
    }

    public ArrayList<Person> getPeople(){
        return listPeople;
    }

}
