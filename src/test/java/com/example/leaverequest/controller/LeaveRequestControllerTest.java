package com.example.leaverequest.controller;

import com.example.leaverequest.dto.LeaveRequestDTO;
import com.example.leaverequest.model.LeaveRequest;
import com.example.leaverequest.model.Status;
import com.example.leaverequest.service.LeaveRequestService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LeaveRequestControllerTest extends ControllerTest
{
    private MockMvc mockMvc;
    
    @Mock
    LeaveRequestService leaveRequestService;
    
    @InjectMocks
    LeaveRequestController classUnderTest;
    
    @Captor
    ArgumentCaptor<LeaveRequestDTO> dtoCaptor;
    
    @Before
    public void setUpControllerTestSupport()
    {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(classUnderTest).build();
    }
    
    @Test
    public void createLeaveRequest() throws Exception
    {
        LeaveRequestDTO dto = new LeaveRequestDTO(1L, "Annual", new Date(), new Date(), 10, new Date(), new Date(), "NEW");
        
        mockMvc.perform(post("/api/leaverequest")
                .accept(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    
        Mockito.verify(leaveRequestService).createLeaveRequest(dtoCaptor.capture());
        Mockito.verifyNoMoreInteractions(leaveRequestService);
        
        LeaveRequestDTO value = dtoCaptor.getValue();
        assertEquals(value.getPersonId(), 1L);
    }
    
    @Test
    public void getAllLeaveRequests() throws Exception
    {
        //Given
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        leaveRequests.add(new LeaveRequest().setStatus(Status.APPROVED).setId(2L));
        Mockito.when(leaveRequestService.getAllLeaveRequests()).thenReturn(leaveRequests);
    
        mockMvc.perform(get("/api/leaverequest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].status", is(Status.APPROVED)))
                .andExpect(jsonPath("$[0].id", is(2)));
    }
    
    @Test
    public void getAllLeaveRequestsInWaiting() throws Exception
    {
        //Given
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        leaveRequests.add(new LeaveRequest().setStatus(Status.WAITINGAPPROVAL).setId(2L));
        Mockito.when(leaveRequestService.getAllLeaveRequestsInWaiting()).thenReturn(leaveRequests);
        
        mockMvc.perform(get("/api/leaverequest/waiting"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].status", is(Status.WAITINGAPPROVAL)))
                .andExpect(jsonPath("$[0].id", is(2)));
    }
    
    @Test
    public void getAllLeaveRequestsByPersonId() throws Exception
    {
        //Given
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        leaveRequests.add(new LeaveRequest().setStatus(Status.APPROVED).setPersonId(2L));
        leaveRequests.add(new LeaveRequest().setStatus(Status.REJECTED).setPersonId(2L));
        Mockito.when(leaveRequestService.getAllLeaveRequestsByPersonId(2L)).thenReturn(leaveRequests);
    
        mockMvc.perform(get("/api/leaverequest/person/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].status", is(Status.APPROVED)))
                .andExpect(jsonPath("$[0].personId", is(2)))
                .andExpect(jsonPath("$[1].status", is(Status.REJECTED)))
                .andExpect(jsonPath("$[1].personId", is(2)));
    }
    
    @Test
    public void getLeaveRequestById() throws Exception
    {
        //Given
        LeaveRequest returnedLeaveRequest = new LeaveRequest().setId(1L).setStatus(Status.APPROVED);
        Mockito.when(leaveRequestService.getLeaveRequestById(1L)).thenReturn(returnedLeaveRequest);
        
        mockMvc.perform(get("/api/leaverequest/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Status.APPROVED)));
    }
    
    @Test
    public void updateLeaveRequestStatusApproved() throws Exception
    {
        //Given
        LeaveRequest returnedLeaveRequest = new LeaveRequest().setId(1L).setStatus(Status.APPROVED);
        Mockito.when(leaveRequestService.updateLeaveRequestStatusApproved(1L)).thenReturn(returnedLeaveRequest);
    
        mockMvc.perform(put("/api/leaverequest/1/changestatus/approved"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Status.APPROVED)));
    }
    
    @Test
    public void updateLeaveRequestStatusRejected() throws Exception
    {
        //Given
        LeaveRequest returnedLeaveRequest = new LeaveRequest().setId(1L).setStatus(Status.REJECTED);
        Mockito.when(leaveRequestService.updateLeaveRequestStatusRejected(1L)).thenReturn(returnedLeaveRequest);
        
        mockMvc.perform(put("/api/leaverequest/1/changestatus/rejected"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Status.REJECTED)));
    }
}