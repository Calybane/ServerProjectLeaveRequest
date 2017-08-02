package com.example.leaverequest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LeaveRequestDTO
{
    private Long id;
    
    @Min(1)
    private final Long personId;
    
    @NotBlank
    @Size(max = 100)
    private final String typeAbsence;
    
    @NotNull
    private final Date leaveFrom;
    
    @NotNull
    private final Date leaveTo;
    
    @Min(1)
    private final Integer daysTaken;
    
    @NotNull
    private final Date requestDate;
    
    private final Date approvalManagerDate;
    
    private final Date approvalHRDate;
    
    @NotNull
    private final String status;
    
    @NotNull
    private final String author;
    
    private final String description;
    
    @JsonCreator
    public LeaveRequestDTO(
            @JsonProperty("id") Long id,
            @JsonProperty("personId") Long personId,
            @JsonProperty("typeAbsence") String typeAbsence,
            @JsonProperty("leaveFrom") Date leaveFrom,
            @JsonProperty("leaveTo") Date leaveTo,
            @JsonProperty("daysTaken") int daysTaken,
            @JsonProperty("requestDate") Date requestDate,
            @JsonProperty("approvalManagerDate") Date approvalManagerDate,
            @JsonProperty("approvalHRDate") Date approvalHRDate,
            @JsonProperty("status") String status,
            @JsonProperty("author") String author,
            @JsonProperty("description") String description) {
        if(id != null) this.id = id;
        this.personId = personId;
        this.typeAbsence = typeAbsence;
        this.leaveFrom = leaveFrom;
        this.leaveTo = leaveTo;
        this.daysTaken = daysTaken;
        this.requestDate = requestDate;
        this.approvalManagerDate = approvalManagerDate;
        this.approvalHRDate = approvalHRDate;
        this.status = status;
        this.author = author;
        this.description = description;
    }
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public long getPersonId()
    {
        return personId;
    }
    
    public String getTypeAbsence()
    {
        return typeAbsence;
    }
    
    public Date getLeaveFrom()
    {
        return leaveFrom;
    }
    
    public Date getLeaveTo()
    {
        return leaveTo;
    }
    
    public int getDaysTaken()
    {
        return daysTaken;
    }
    
    public Date getRequestDate()
    {
        return requestDate;
    }
    
    public Date getApprovalManagerDate()
    {
        return approvalManagerDate;
    }
    
    public Date getApprovalHRDate()
    {
        return approvalHRDate;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public String getAuthor()
    {
        return author;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    @Override
    public String toString()
    {
        return "LeaveRequestDTO{" +
                "id=" + id +
                ", personId=" + personId +
                ", typeAbsence='" + typeAbsence + '\'' +
                ", leaveFrom=" + leaveFrom +
                ", leaveTo=" + leaveTo +
                ", daysTaken=" + daysTaken +
                ", requestDate=" + requestDate +
                ", approvalManagerDate=" + approvalManagerDate +
                ", approvalHRDate=" + approvalHRDate +
                ", status='" + status + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
