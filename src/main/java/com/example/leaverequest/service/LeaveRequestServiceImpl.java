package com.example.leaverequest.service;

import com.example.leaverequest.dto.LeaveRequestDTO;
import com.example.leaverequest.exception.EntityBadInformationsException;
import com.example.leaverequest.exception.EntityNotFoundException;
import com.example.leaverequest.model.LeaveRequest;
import com.example.leaverequest.model.Status;
import com.example.leaverequest.model.TypesAbsence;
import com.example.leaverequest.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService
{
    @Autowired
    LeaveRequestRepository leaveRequestRepository;
    
    
    // Not used
    @Override
    @PreAuthorize("isAuthenticated()")
    public List<LeaveRequest> getAllLeaveRequests()
    {
        return leaveRequestRepository.findAll();
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public List<LeaveRequest> getAllLeaveRequestsNotRejected()
    {
        return leaveRequestRepository.findAllByStatusNotLike(Status.REJECTED.getStatus());
    }
    
    @Override
    @PreAuthorize("isAuthenticated()")
    public Page<LeaveRequest> getAllLeaveRequests(Pageable pageable)
    {
        return leaveRequestRepository.findAll(pageable);
    }
    
    @Override
    @PreAuthorize("isAuthenticated()")
    public Page<LeaveRequest> getAllLeaveRequestsByPersonId(long personId, Pageable pageable)
    {
        return leaveRequestRepository.findAllByPersonId(personId, pageable);
    }
    
    @Override
    @PreAuthorize("isAuthenticated()")
    public List<Date> getAllDisabledDatesByPersonId(long personId)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -2);
        
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findAllByPersonIdAndLeaveFromIsAfter(personId, c.getTime());
        List<Date> dates = new ArrayList<>();
        for (LeaveRequest request : leaveRequests) {
            Date start = request.getLeaveFrom();
            Date end = request.getLeaveTo();
            while (start.compareTo(end) <= 0) {
                dates.add(start);
                c.setTime(start);
                c.add(Calendar.DAY_OF_MONTH, 1);
                start = c.getTime();
            }
        }
        return dates;
    }
    
    @Override
    @PreAuthorize("isAuthenticated()")
    public Page<LeaveRequest> getAllLeaveRequestsByStatus(String status, Pageable pageable)
    {
        return leaveRequestRepository.findAllByStatusLike(status, pageable);
    }
    
    @Override
    @PreAuthorize("isAuthenticated()")
    public LeaveRequest getLeaveRequestById(long id)
    {
        return leaveRequestRepository.findOne(id);
    }
    
    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public LeaveRequest createLeaveRequest(LeaveRequestDTO dto)
    {
        LeaveRequest leaveRequest = new LeaveRequest(dto);
        leaveRequest.setStatus(Status.WAITINGAPPROVAL.getStatus());
        if (leaveRequest.valid(getAllDisabledDatesByPersonId(leaveRequest.getPersonId())))
        {
            return leaveRequestRepository.save(leaveRequest);
        }
        else
        {
            throw new EntityBadInformationsException("Leave request informations are incorrect");
        }
    }
    
    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public LeaveRequest updateLeaveRequestApprovedByManager(long id)
    {
        LeaveRequest leaveRequest = leaveRequestRepository.findOne(id);
        if(leaveRequest == null)
        {
            throw new EntityNotFoundException("Leave request with id " + id + " not found.");
        }
        else
        {
            leaveRequest.setStatus(Status.APPROVEDMANAGER.getStatus());
            leaveRequest.setApprovalManagerDate(new Date());
            return leaveRequestRepository.save(leaveRequest);
        }
    }
    
    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HR')")
    public LeaveRequest updateLeaveRequestApprovedByHR(long id)
    {
        LeaveRequest leaveRequest = leaveRequestRepository.findOne(id);
        if(leaveRequest == null)
        {
            throw new EntityNotFoundException("Leave request with id " + id + " not found.");
        }
        else
        {
            leaveRequest.setStatus(Status.APPROVEDHR.getStatus());
            leaveRequest.setApprovalHRDate(new Date());
            return leaveRequestRepository.save(leaveRequest);
        }
    }
    
    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HR')")
    public LeaveRequest updateLeaveRequestRejected(long id)
    {
        LeaveRequest leaveRequest = leaveRequestRepository.findOne(id);
        if(leaveRequest == null)
        {
            throw new EntityNotFoundException("Leave request with id " + id + " not found.");
        }
        else
        {
            leaveRequest.setStatus(Status.REJECTED.getStatus());
            if(leaveRequest.getApprovalManagerDate() == null) leaveRequest.setApprovalManagerDate(new Date());
            else leaveRequest.setApprovalHRDate(new Date());
            return leaveRequestRepository.save(leaveRequest);
        }
    }
    
    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void deleteLeaveRequest(long id)
    {
        LeaveRequest leaveRequest = leaveRequestRepository.findOne(id);
        if (leaveRequest.getStatus().equals(Status.WAITINGAPPROVAL.getStatus())) {
            leaveRequestRepository.delete(id);
        } else {
            throw new EntityBadInformationsException("This leave request is already in approbation");
        }
    }
    
    @Override
    @PreAuthorize("isAuthenticated()")
    public String[] getAllTypesAbsence()
    {
        return TypesAbsence.typesAbsence();
    }
}
