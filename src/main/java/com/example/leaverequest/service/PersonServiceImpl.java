package com.example.leaverequest.service;

import com.example.leaverequest.dto.PersonDTO;
import com.example.leaverequest.model.Person;
import com.example.leaverequest.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService
{
    @Autowired
    PersonRepository personRepository;
    
    @Override
    public Person createPerson(PersonDTO dto)
    {
        Person person = new Person(dto);
        return personRepository.save(person);
    }
    
    @Override
    public Person getPersonById(long id)
    {
        return personRepository.findOne(id);
    }
}
