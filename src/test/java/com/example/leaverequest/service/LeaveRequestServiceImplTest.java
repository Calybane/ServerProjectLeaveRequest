package com.example.leaverequest.service;

import com.example.leaverequest.dto.LeaveRequestDTO;
import com.example.leaverequest.exception.EntityNotFoundException;
import com.example.leaverequest.model.LeaveRequest;
import com.example.leaverequest.model.Status;
import com.example.leaverequest.repository.LeaveRequestRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class LeaveRequestServiceImplTest
{
    @Mock
    LeaveRequestRepository leaveRequestRepository;
    
    @InjectMocks
    LeaveRequestServiceImpl classUnderTest;
    
    @Test
    public void createLeaveRequest() {
        //Given
        LeaveRequest returnedLeaveRequest = Mockito.mock(LeaveRequest.class);
        LeaveRequestDTO dto = new LeaveRequestDTO(1L, "Annual leave", new Date(), new Date(), 1, new Date(), null, "Waiting for approval");
        Mockito.when(leaveRequestRepository.save(any(LeaveRequest.class))).thenReturn(returnedLeaveRequest);
        
        //When
        LeaveRequest leaveRequest = classUnderTest.createLeaveRequest(dto);
        
        //Then
        assertEquals(leaveRequest, returnedLeaveRequest);
    }
    
    @Test
    public void getAllLeaveRequests() {
        //Given
        LeaveRequest leaveRequest = Mockito.mock(LeaveRequest.class);
        
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        leaveRequests.add(leaveRequest);
        Mockito.when(leaveRequestRepository.findAll(new PageRequest(0, 10)))
                .thenReturn(new PageImpl<>(leaveRequests, new PageRequest(0,10), 100));
    
        //When
        Page<LeaveRequest> allLeaveRequests = classUnderTest.getAllLeaveRequests(new PageRequest(0, 10));
    
        //Then
        assertEquals(allLeaveRequests.getTotalElements(), 100);
        assertEquals(allLeaveRequests.getSize(), 10);
        assertEquals(allLeaveRequests.getContent().get(0), leaveRequest);
    }
    
    @Test
    public void getAllLeaveRequestsByPersonId() {
        //Given
        LeaveRequest leaveRequest = Mockito.mock(LeaveRequest.class);
        
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        leaveRequests.add(leaveRequest);
        Mockito.when(leaveRequestRepository.findAllByPersonId(1L, new PageRequest(0, 10)))
                .thenReturn(new PageImpl<>(leaveRequests, new PageRequest(0,10), 100));
        
        //When
        Page<LeaveRequest> allLeaveRequests = classUnderTest.getAllLeaveRequestsByPersonId(1L, new PageRequest(0, 10));
        
        //Then
        assertEquals(allLeaveRequests.getTotalElements(), 100);
        assertEquals(allLeaveRequests.getSize(), 10);
        assertEquals(allLeaveRequests.getContent().get(0), leaveRequest);
    }
    
    @Test
    public void getAllLeaveRequestsBystatus() {
        //Given
        LeaveRequest leaveRequest = Mockito.mock(LeaveRequest.class);
        
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        leaveRequests.add(leaveRequest);
        Mockito.when(leaveRequestRepository.findAllByStatusLike(Status.WAITINGAPPROVAL, new PageRequest(0, 10))).thenReturn(new PageImpl<>(leaveRequests, new PageRequest(0,10), 100));
        
        //When
        Page<LeaveRequest> allLeaveRequests = classUnderTest.getAllLeaveRequestsByStatus(Status.WAITINGAPPROVAL, new PageRequest(0, 10));
        
        //Then
        assertEquals(allLeaveRequests.getTotalElements(), 100);
        assertEquals(allLeaveRequests.getSize(), 10);
        assertEquals(allLeaveRequests.getContent().get(0), leaveRequest);
    }
    
    @Test
    public void getLeaveRequestById() {
        //Given
        LeaveRequest returnedLeaveRequest = Mockito.mock(LeaveRequest.class);
        Mockito.when(leaveRequestRepository.findOne(1L)).thenReturn(returnedLeaveRequest);
        
        //When
        LeaveRequest leaveRequest = classUnderTest.getLeaveRequestById(1L);
        
        //Then
        assertEquals(leaveRequest, returnedLeaveRequest);
    }
    
    @Test(expected = EntityNotFoundException.class)
    public void approveInexistingLeaveRequest() {
        Mockito.when(leaveRequestRepository.findOne(456L)).thenReturn(null);
        classUnderTest.updateLeaveRequestApproved(456L);
    }
    
    @Test
    public void approveExistingLeaveRequest() {
        //Given
        LeaveRequest mockedLeaveRequest = Mockito.mock(LeaveRequest.class);
        Mockito.when(leaveRequestRepository.findOne(2L)).thenReturn(mockedLeaveRequest);
    
        //When
        classUnderTest.updateLeaveRequestApproved(2L);
        
        //Then
        Mockito.verify(mockedLeaveRequest).setStatus(Status.APPROVED);
        Mockito.verify(leaveRequestRepository).save(mockedLeaveRequest);
    }
    
    @Test
    public void rejectExistingLeaveRequest() {
        //Given
        LeaveRequest mockedLeaveRequest = Mockito.mock(LeaveRequest.class);
        LeaveRequest savedLeaveRequest = Mockito.mock(LeaveRequest.class);
        Mockito.when(leaveRequestRepository.findOne(2L)).thenReturn(mockedLeaveRequest);
        Mockito.when(leaveRequestRepository.save(mockedLeaveRequest)).thenReturn(savedLeaveRequest);
        
        //When
        LeaveRequest leaveRequest = classUnderTest.updateLeaveRequestRejected(2L);
    
        //Then
        Mockito.verify(mockedLeaveRequest).setStatus(Status.REJECTED);
        assertEquals(savedLeaveRequest, leaveRequest);
    }
}