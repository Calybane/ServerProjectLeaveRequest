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
    
    @Min(1)
    @Column(nullable = false)
    private long personId;
    
    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String typeAbsence;
    
    @Column(nullable = false)
    private Date leaveFrom;
    
    @Column(nullable = false)
    private Date leaveTo;
    
    @Min(1)
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
    
    public void setId(long id)
    {
        this.id = id;
    }
    
    public long getPersonId()
    {
        return personId;
    }
    
    public void setPersonId(long personId)
    {
        this.personId = personId;
    }
    
    public String getTypeAbsence()
    {
        return typeAbsence;
    }
    
    public void setTypeAbsence(String typeAbsence)
    {
        this.typeAbsence = typeAbsence;
    }
    
    public Date getLeaveFrom()
    {
        return leaveFrom;
    }
    
    public void setLeaveFrom(Date leaveFrom)
    {
        this.leaveFrom = leaveFrom;
    }
    
    public Date getLeaveTo()
    {
        return leaveTo;
    }
    
    public void setLeaveTo(Date leaveTo)
    {
        this.leaveTo = leaveTo;
    }
    
    public int getDaysTaken()
    {
        return daysTaken;
    }
    
    public void setDaysTaken(int daysTaken)
    {
        this.daysTaken = daysTaken;
    }
    
    public Date getRequestDate()
    {
        return requestDate;
    }
    
    public void setRequestDate(Date requestDate)
    {
        this.requestDate = requestDate;
    }
    
    public Date getApprovalDate()
    {
        return approvalDate;
    }
    
    public void setApprovalDate(Date approvalDate)
    {
        this.approvalDate = approvalDate;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
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
