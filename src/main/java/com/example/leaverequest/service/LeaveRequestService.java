package com.example.leaverequest.service;

import com.example.leaverequest.dto.LeaveRequestDTO;
import com.example.leaverequest.model.LeaveRequest;

import java.util.List;

public interface LeaveRequestService
{
    LeaveRequest createLeaveRequest(LeaveRequestDTO dto);
    
    List<LeaveRequest> getAllLeaveRequests();
    
    List<LeaveRequest> getAllLeaveRequestsInWaiting();
    
    List<LeaveRequest> getAllLeaveRequestsByPersonId(long personId);
    
    LeaveRequest getLeaveRequestById(long id);
    
    LeaveRequest updateLeaveRequestApproved(long id);
    
    LeaveRequest updateLeaveRequestRejected(long id);
}
