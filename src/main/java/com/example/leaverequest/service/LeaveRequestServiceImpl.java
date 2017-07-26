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

import java.util.Date;
import java.util.List;

@Service
@PreAuthorize("authenticated")
public class LeaveRequestServiceImpl implements LeaveRequestService
{
    @Autowired
    LeaveRequestRepository leaveRequestRepository;
    
    @Override
    public LeaveRequest createLeaveRequest(LeaveRequestDTO dto)
    {
        LeaveRequest leaveRequest = new LeaveRequest(dto);
        if (leaveRequest.valid())
        {
            return leaveRequestRepository.save(leaveRequest);
        }
        else
        {
            throw new EntityBadInformationsException("Leave request informations are incorrect");
        }
    }
    
    // Not used
    @Override
    public List<LeaveRequest> getAllLeaveRequests()
    {
        return leaveRequestRepository.findAll();
    }
    
    @Override
    public Page<LeaveRequest> getAllLeaveRequests(Pageable pageable)
    {
        return leaveRequestRepository.findAll(pageable);
    }
    
    @Override
    public Page<LeaveRequest> getAllLeaveRequestsByPersonId(long personId, Pageable pageable)
    {
        return leaveRequestRepository.findAllByPersonId(personId, pageable);
    }
    
    @Override
    public Page<LeaveRequest> getAllLeaveRequestsByStatus(String status, Pageable pageable)
    {
        return leaveRequestRepository.findAllByStatusLike(status, pageable);
    }
    
    @Override
    public LeaveRequest getLeaveRequestById(long id)
    {
        return leaveRequestRepository.findOne(id);
    }
    
    @Override
    public LeaveRequest updateLeaveRequestApproved(long id)
    {
        LeaveRequest leaveRequest = leaveRequestRepository.findOne(id);
        if(leaveRequest == null)
        {
            throw new EntityNotFoundException("Leave request with id " + id + " not found.");
        }
        else
        {
            leaveRequest.setStatus(Status.APPROVED);
            leaveRequest.setApprovalDate(new Date());
            return leaveRequestRepository.save(leaveRequest);
        }
    }
    
    @Override
    public LeaveRequest updateLeaveRequestRejected(long id)
    {
        LeaveRequest leaveRequest = leaveRequestRepository.findOne(id);
        if(leaveRequest == null)
        {
            throw new EntityNotFoundException("Leave request with id " + id + " not found.");
        }
        else
        {
            leaveRequest.setStatus(Status.REJECTED);
            leaveRequest.setApprovalDate(new Date());
            return leaveRequestRepository.save(leaveRequest);
        }
    }
    
    @Override
    public String[] getAllTypesAbsence()
    {
        return TypesAbsence.typesAbsence();
    }
}
