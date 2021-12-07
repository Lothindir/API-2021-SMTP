package ch.heigvd.mail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import lombok.NoArgsConstructor;

/**
 * Wrapper over a List of {@link Person}
 *
 * @author Anthony Coke
 * @author Francesco Monti
 */
@NoArgsConstructor
public class Group implements Iterable<Person> {
   List<Person> members = new ArrayList<>();

   public Group(Group other) {
      this.members = new ArrayList<>(other.members);
   }

   public Group(List<Person> members) {
      this.members = new ArrayList<>(members);
   }

   /**
    * Retrieves the people in the group.
    * 
    * @return a list of Person that are part of the group.
    */
   public List<Person> getMembers() {
      return new ArrayList<>(members);
   }

   /**
    * Sets the people of the group.
    * 
    * @param newPeople a list of Person representing the group.
    */
   public void setMembers(List<Person> newPeople) {
      members = new ArrayList<>(newPeople);
   }

   public void shuffle() {
      Collections.shuffle(members);
   }

   public void add(Person p) {
      members.add(p);
   }

   public Person remove(int index) {
      return members.remove(index);
   }

   public int size() {
      return members.size();
   }

   @Override
   public Iterator<Person> iterator() {
      return this.members.iterator();
   }
}
