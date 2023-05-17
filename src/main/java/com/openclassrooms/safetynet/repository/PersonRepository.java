package com.openclassrooms.safetynet.repository;

import com.openclassrooms.safetynet.model.Person;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PersonRepository {

    private List<Person> persons = new ArrayList<>();

    public List<Person> getPersons(){
        return this.persons;
    }

    public void setPersons(List<Person> persons){
        this.persons = persons;
    }

    public Person findPersonByFirstNameAndLastName(String firstName,String lastName){
        Person personResult = null;
        for(Person person : this.persons){
            if (person.getFirstName().toLowerCase().equals(firstName.toLowerCase()) && person.getLastName().toLowerCase().equals(lastName.toLowerCase())){
                personResult = person;
            }
        }
        return personResult;
    }

    public Person addPerson(Person person){
        this.persons.add(person);
        return person;
    }

    public void deletePerson(Person person){
        this.persons.remove(person);
    }

    public void updatePerson(Person person) {
        Person updatePerson = this.findPersonByFirstNameAndLastName(person.getFirstName(), person.getLastName());
        updatePerson.setAddress(person.getAddress());
        updatePerson.setCity(person.getCity());
        updatePerson.setPhone(person.getPhone());
        updatePerson.setZip(person.getZip());
        updatePerson.setEmail(person.getEmail());
        }

}
