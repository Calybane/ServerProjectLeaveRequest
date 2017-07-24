package com.example.leaverequest.service;

import com.example.leaverequest.dto.LeaveRequestDTO;
import com.example.leaverequest.model.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface LeaveRequestService
{
    LeaveRequest createLeaveRequest(LeaveRequestDTO dto);
    
    List<LeaveRequest> getAllLeaveRequests();
    
    Page<LeaveRequest> getAllLeaveRequests(Pageable pageable);
    
    List<LeaveRequest> getAllLeaveRequestsByStatus(String status, Sort sort);
    
    List<LeaveRequest> getAllLeaveRequestsByPersonId(long personId);
    
    LeaveRequest getLeaveRequestById(long id);
    
    LeaveRequest updateLeaveRequestApproved(long id);
    
    LeaveRequest updateLeaveRequestRejected(long id);
    
    String[] getAllTypesAbsence();
}
