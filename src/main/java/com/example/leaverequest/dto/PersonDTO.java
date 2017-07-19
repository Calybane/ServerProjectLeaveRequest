package com.example.leaverequest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class PersonDTO
{
    private Long id;
    
    @NotBlank
    @Size(max = 255)
    private final String lastname;
    
    @NotBlank
    @Size(max = 255)
    private final String firstname;
    
    @Min(0)
    private final Integer daysLeft;
    
    @NotBlank
    @Size(max = 255)
    private String role;
    
    @JsonCreator
    public PersonDTO(
            @JsonProperty("lastname") String lastname,
            @JsonProperty("firstname") String firstname,
            @JsonProperty("daysLeft") Integer daysLeft,
            @JsonProperty("role") String role) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.daysLeft = daysLeft;
        this.role = role;
    }
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getLastname()
    {
        return lastname;
    }
    
    public String getFirstname()
    {
        return firstname;
    }
    
    public Integer getDaysLeft()
    {
        return daysLeft;
    }
    
    public String getRole()
    {
        return role;
    }
    
    @Override
    public String toString()
    {
        return "PersonDTO{" +
                "id=" + id +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", daysLeft=" + daysLeft +
                ", role='" + role + '\'' +
                '}';
    }
}
