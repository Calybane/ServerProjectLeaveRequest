package com.example.leaverequest.service;

import com.example.leaverequest.dto.LeaveRequestDTO;
import com.example.leaverequest.model.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Date;
import java.util.List;

public interface LeaveRequestService
{
    @PreAuthorize("isAuthenticated()")
    LeaveRequest createLeaveRequest(LeaveRequestDTO dto);
    
    @PreAuthorize("isAuthenticated()")
    List<LeaveRequest> getAllLeaveRequests();
    
    @PreAuthorize("isAuthenticated()")
    Page<LeaveRequest> getAllLeaveRequests(Pageable pageable);
    
    @PreAuthorize("isAuthenticated()")
    Page<LeaveRequest> getAllLeaveRequestsByPersonId(long personId, Pageable pageable);
    
    @PreAuthorize("isAuthenticated()")
    Page<LeaveRequest> getAllLeaveRequestsByStatus(String status, Pageable pageable);
    
    @PreAuthorize("isAuthenticated()")
    LeaveRequest getLeaveRequestById(long id);
    
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    LeaveRequest updateLeaveRequestApprovedByManager(long id);
    
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HR')")
    LeaveRequest updateLeaveRequestApprovedByHR(long id);
    
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HR')")
    LeaveRequest updateLeaveRequestRejected(long id);
    
    @PreAuthorize("isAuthenticated()")
    void deleteLeaveRequest(long id);
    
    @PreAuthorize("isAuthenticated()")
    String[] getAllTypesAbsence();
}
