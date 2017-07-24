package com.example.leaverequest.model;

import com.example.leaverequest.dto.LeaveRequestDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.text.*;
import java.util.Arrays;
import java.util.Date;

@Entity
@Table(name = "LEAVE_REQUEST")
public class LeaveRequest
{
    @Id
    @Column(nullable = false, name = "ID")
    /*@Column(nullable = false)*/
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(nullable = false, name = "PERSON_ID")
    private long personId;
    
    @Column(nullable = false, name = "TYPE_ABSENCE")
    private String typeAbsence;
    
    @Column(nullable = false, name = "LEAVE_FROM")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date leaveFrom;
    
    @Column(nullable = false, name = "LEAVE_TO")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date leaveTo;
    
    @Column(nullable = false, name = "DAYS_TAKEN")
    private int daysTaken;
    
    @Column(nullable = false, name = "REQUEST_DATE")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date requestDate;
    
    @Column(name = "APPROVAL_DATE")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date approvalDate;
    
    @Column(nullable = false, name = "STATUS")
    private String status;
    
    
    public LeaveRequest()
    {
    }
    
    public LeaveRequest(LeaveRequestDTO dto)
    {
        this.personId = dto.getPersonId();
        this.typeAbsence = dto.getTypeAbsence();
        this.leaveFrom = dto.getLeaveFrom();
        this.leaveTo = dto.getLeaveTo();
        this.daysTaken = dto.getDaysTaken();
        this.requestDate = dto.getRequestDate();
        this.approvalDate = dto.getApprovalDate();
        this.status = dto.getStatus();
    }
    
    
    public long getId()
    {
        return id;
    }
    
    public LeaveRequest setId(long id)
    {
        this.id = id;
        return this;
    }
    
    public long getPersonId()
    {
        return personId;
    }
    
    public LeaveRequest setPersonId(long personId)
    {
        this.personId = personId;
        return this;
    }
    
    public String getTypeAbsence()
    {
        return typeAbsence;
    }
    
    public LeaveRequest setTypeAbsence(String typeAbsence)
    {
        this.typeAbsence = typeAbsence;
        return this;
    }
    
    public Date getLeaveFrom()
    {
        return leaveFrom;
    }
    
    public LeaveRequest setLeaveFrom(Date leaveFrom)
    {
        this.leaveFrom = leaveFrom;
        return this;
    }
    
    public Date getLeaveTo()
    {
        return leaveTo;
    }
    
    public LeaveRequest setLeaveTo(Date leaveTo)
    {
        this.leaveTo = leaveTo;
        return this;
    }
    
    public int getDaysTaken()
    {
        return daysTaken;
    }
    
    public LeaveRequest setDaysTaken(int daysTaken)
    {
        this.daysTaken = daysTaken;
        return this;
    }
    
    public Date getRequestDate()
    {
        return requestDate;
    }
    
    public LeaveRequest setRequestDate(Date requestDate)
    {
        this.requestDate = requestDate;
        return this;
    }
    
    public Date getApprovalDate()
    {
        return approvalDate;
    }
    
    public LeaveRequest setApprovalDate(Date approvalDate)
    {
        this.approvalDate = approvalDate;
        return this;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public LeaveRequest setStatus(String status)
    {
        this.status = status;
        return this;
    }
    
    @Override
    public String toString()
    {
        return "LeaveRequest{" +
                "id=" + id +
                ", personId=" + personId +
                ", typeAbsence='" + typeAbsence + '\'' +
                ", leaveFrom=" + leaveFrom +
                ", leaveTo=" + leaveTo +
                ", daysTaken=" + daysTaken +
                ", requestDate=" + requestDate +
                ", approvalDate=" + approvalDate +
                ", status='" + status + '\'' +
                '}';
    }
    
    public boolean valid()
    {
        // TODO : verify the days taken with the days left of the user, when the users will be taking in count
        
        if(this.leaveFrom.after(this.leaveTo)) return false;
        if(this.daysTaken <= 0) return false;
        String[] typesAbsence = TypesAbsence.typesAbsence();
        if(Arrays.stream(typesAbsence).noneMatch(type -> type.equalsIgnoreCase(this.typeAbsence))) return false;
        
        return true;
    }
}
