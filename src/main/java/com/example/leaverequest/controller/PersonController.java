package com.example.leaverequest.controller;

import com.example.leaverequest.dto.PersonDTO;
import com.example.leaverequest.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api/person")
public class PersonController
{
    @Autowired
    PersonService personService;
    
    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createPerson(@Valid @RequestBody final PersonDTO dto)
    {
        System.out.println("Creating dto : " + dto.toString());
        return new ResponseEntity(personService.createPerson(dto), HttpStatus.CREATED);
    }
    
    
    @RequestMapping(method = GET, path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDTO> getPersonById(@PathVariable(value = "id") final long id)
    {
        return new ResponseEntity(personService.getPersonById(id), HttpStatus.OK);
    }
    
    
    @RequestMapping(method = GET, path = "/roles", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDTO> getRoles()
    {
        return new ResponseEntity(SecurityContextHolder.getContext().getAuthentication().getAuthorities(), HttpStatus.OK);
    }
}
