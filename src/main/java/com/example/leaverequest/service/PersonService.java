package com.example.leaverequest.service;

import com.example.leaverequest.dto.PersonDTO;
import com.example.leaverequest.model.Person;

public interface PersonService
{
    Person createPerson(PersonDTO dto);
    
    Person getPersonById(long id);
}
