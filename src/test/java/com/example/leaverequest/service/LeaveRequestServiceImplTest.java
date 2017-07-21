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
import org.springframework.data.domain.Sort;

import java.util.Arrays;
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
        LeaveRequestDTO dto = new LeaveRequestDTO(1L, "Annual", new Date(), new Date(), 10, new Date(), new Date(), "NEW");
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
        Mockito.when(leaveRequestRepository.findAll()).thenReturn(Arrays.asList(leaveRequest));
    
        //When
        List<LeaveRequest> allLeaveRequests = classUnderTest.getAllLeaveRequests();
    
        //Then
        assertEquals(allLeaveRequests.size(), 1);
        assertEquals(allLeaveRequests.get(0), leaveRequest);
    }
    
    @Test
    public void getAllLeaveRequestsInWaiting() {
        //Given
        LeaveRequest leaveRequest = Mockito.mock(LeaveRequest.class);
        Mockito.when(leaveRequestRepository.findAllByStatusLike(Status.WAITINGAPPROVAL, new Sort(Sort.Direction.ASC, "leaveFrom")))
                .thenReturn(Arrays.asList(leaveRequest));
        
        //When
        List<LeaveRequest> allLeaveRequests = classUnderTest.getAllLeaveRequestsInWaiting();
        
        //Then
        assertEquals(allLeaveRequests.size(), 1);
        assertEquals(allLeaveRequests.get(0), leaveRequest);
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
        Mockito.when(leaveRequestRepository.findOne(2L)).thenReturn(null);
        classUnderTest.updateLeaveRequestApproved(2L);
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