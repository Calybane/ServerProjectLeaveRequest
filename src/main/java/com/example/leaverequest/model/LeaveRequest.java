package com.example.leaverequest.model;

import com.example.leaverequest.dto.LeaveRequestDTO;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table
public class LeaveRequest
{
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(nullable = false)
    private long personId;
    
    @Column(nullable = false)
    private String typeAbsence;
    
    @Column(nullable = false)
    private Date leaveFrom;
    
    @Column(nullable = false)
    private Date leaveTo;
    
    @Column(nullable = false)
    private int daysTaken;
    
    @Column(nullable = false)
    private Date requestDate;
    
    @Column
    private Date approvalDate;
    
    @Column
    private String status;
    
    
    public LeaveRequest(){}
    
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
}
