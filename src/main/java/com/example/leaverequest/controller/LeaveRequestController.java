package com.example.leaverequest.controller;

import com.example.leaverequest.dto.LeaveRequestDTO;
import com.example.leaverequest.model.LeaveRequest;
import com.example.leaverequest.model.Status;
import com.example.leaverequest.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/api/leaverequest")
public class LeaveRequestController
{
    @Autowired
    LeaveRequestService leaveRequestService;
    
    
    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    public Page<LeaveRequest> getAllLeaveRequests(final Pageable pageable)
    {
        return leaveRequestService.getAllLeaveRequests(pageable);
    }
    
    @RequestMapping(method = GET, path = "/views", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LeaveRequest> getAllLeaveRequestsViews()
    {
        return new ResponseEntity(leaveRequestService.getAllLeaveRequestsNotRejected(), HttpStatus.OK);
    }
    
    @RequestMapping(method = GET, path = "/person/{id}", produces = APPLICATION_JSON_VALUE)
    public Page<LeaveRequest> getAllLeaveRequestsByPersonId(@PathVariable(value = "id") final long id, final Pageable pageable)
    {
        return leaveRequestService.getAllLeaveRequestsByPersonId(id, pageable);
    }
    
    @RequestMapping(method = GET, path = "/person/{id}/disableddates", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Date>> getAllDisabledDatesByPersonId(@PathVariable(value = "id") final long id)
    {
        return new ResponseEntity(leaveRequestService.getAllDisabledDatesByPersonId(id), HttpStatus.OK);
    }
    
    @RequestMapping(method = GET, path = "/waiting", produces = APPLICATION_JSON_VALUE)
    public Page<LeaveRequest> getAllLeaveRequestsByStatusWaiting(final Pageable pageable)
    {
        return leaveRequestService.getAllLeaveRequestsByStatus(Status.WAITINGAPPROVAL.getStatus(), pageable);
    }
    
    @RequestMapping(method = GET, path = "/approvedmanager", produces = APPLICATION_JSON_VALUE)
    public Page<LeaveRequest> getAllLeaveRequestsByStatusApprovedByManager(final Pageable pageable)
    {
        return leaveRequestService.getAllLeaveRequestsByStatus(Status.APPROVEDMANAGER.getStatus(), pageable);
    }
    
    @RequestMapping(method = GET, path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LeaveRequest> getLeaveRequestById(@PathVariable(value = "id") final long id)
    {
        return new ResponseEntity(leaveRequestService.getLeaveRequestById(id), HttpStatus.OK);
    }
    
    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LeaveRequest> createLeaveRequest(@Valid @RequestBody final LeaveRequestDTO dto)
    {
        System.out.println("Creating dto : " + dto.toString());
        return new ResponseEntity(leaveRequestService.createLeaveRequest(dto), HttpStatus.CREATED);
    }
    
    @RequestMapping(method = PUT, path = "/{id}/changestatus/approved/manager", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LeaveRequest> updateLeaveRequestStatusApprovedByManager(@PathVariable("id") final long id)
    {
        return new ResponseEntity(leaveRequestService.updateLeaveRequestApprovedByManager(id), HttpStatus.OK);
    }
    
    @RequestMapping(method = PUT, path = "/{id}/changestatus/approved/hr", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LeaveRequest> updateLeaveRequestStatusApprovedByHr(@PathVariable("id") final long id)
    {
        return new ResponseEntity(leaveRequestService.updateLeaveRequestApprovedByHR(id), HttpStatus.OK);
    }
    
    @RequestMapping(method = PUT, path = "/{id}/changestatus/rejected", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LeaveRequest> updateLeaveRequestStatusRejected(@PathVariable("id") final long id)
    {
        return new ResponseEntity(leaveRequestService.updateLeaveRequestRejected(id), HttpStatus.OK);
    }
    
    @RequestMapping(method = DELETE, path = "/{id}")
    public ResponseEntity<LeaveRequest> deleteLeaveRequest(@PathVariable("id") final long id)
    {
        leaveRequestService.deleteLeaveRequest(id);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @RequestMapping(method = GET, path = "/typesabsence", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String[]> getAllTypesAbsence()
    {
        return new ResponseEntity(leaveRequestService.getAllTypesAbsence(), HttpStatus.OK);
    }
}
