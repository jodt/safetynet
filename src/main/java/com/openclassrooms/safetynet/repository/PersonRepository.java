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
}
