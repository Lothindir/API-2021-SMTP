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

   /**
    * Constructor using another group.
    * @param other the other group to copy.
    */
   public Group(Group other) {
      this.members = new ArrayList<>(other.members);
   }

   /**
    * Constructor using a list of members.
    * @param members the list of members to copy.
    */
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

   /**
    * Shuffles the content of the list members.
    */
   public void shuffle() {
      Collections.shuffle(members);
   }

   /**
    * Adds a person to the group.
    * @param p the person to add.
    */
   public void add(Person p) {
      members.add(p);
   }

   /**
    * Removes a person from the members.
    * @param index the index of the person to remove.
    * @return the person removed.
    */
   public Person remove(int index) {
      return members.remove(index);
   }

   /**
    * Gets the size of the list.
    * @return the amount of members in the list.
    */
   public int size() {
      return members.size();
   }

   /**
    * Gets the iterator on the list members.
    * @return an Iterator.
    */
   @Override
   public Iterator<Person> iterator() {
      return this.members.iterator();
   }
}
