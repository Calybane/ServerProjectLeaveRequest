package com.example.leaverequest.model;

import com.example.leaverequest.dto.PersonDTO;

import javax.persistence.*;

@Entity
@Table
public class Person
{
    @Id
    @Column(nullable = false, name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(nullable = false, name = "LASTNAME")
    private String lastname;
    
    @Column(nullable = false, name = "FIRSTNAME")
    private String firstname;
    
    @Column(nullable = false, name = "DAYS_LEFT")
    private Integer daysLeft;
    
    
    public Person(){}
    
    public Person(PersonDTO dto)
    {
        this.lastname = dto.getLastname();
        this.firstname = dto.getFirstname();
        this.daysLeft = dto.getDaysLeft();
    }
    
    
    public long getId()
    {
        return id;
    }
    
    public void setId(long id)
    {
        this.id = id;
    }
    
    public String getLastname()
    {
        return lastname;
    }
    
    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }
    
    public String getFirstname()
    {
        return firstname;
    }
    
    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }
    
    public Integer getDaysLeft()
    {
        return daysLeft;
    }
    
    public void setDaysLeft(Integer daysLeft)
    {
        this.daysLeft = daysLeft;
    }
    
    @Override
    public String toString()
    {
        return "Person{" +
                "id=" + id +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", daysLeft=" + daysLeft +
                '}';
    }
}
