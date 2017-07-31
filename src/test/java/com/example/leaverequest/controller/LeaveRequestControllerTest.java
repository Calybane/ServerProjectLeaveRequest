package com.example.leaverequest.controller;

import com.example.leaverequest.dto.LeaveRequestDTO;
import com.example.leaverequest.model.LeaveRequest;
import com.example.leaverequest.model.Status;
import com.example.leaverequest.model.TypesAbsence;
import com.example.leaverequest.service.LeaveRequestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
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

@EnableSpringDataWebSupport
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners(listeners={ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class LeaveRequestControllerTest extends ControllerTest
{
    private MockMvc mockMvc;
    
    @Mock
    LeaveRequestService leaveRequestService;
    
    @InjectMocks
    LeaveRequestController classUnderTest;
    
    @InjectMocks
    PageableHandlerMethodArgumentResolver pageableArgumentResolver;
    
    @Captor
    ArgumentCaptor<LeaveRequestDTO> dtoCaptor;
    
    @Before
    public void setUpControllerTestSupport()
    {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(classUnderTest)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .build();
    }
    
    @Test
    public void createLeaveRequest() throws Exception
    {
        LeaveRequestDTO dto = new LeaveRequestDTO(1L, "Annual", new Date(), new Date(), 10, new Date(), new Date()
                , null, Status.WAITINGAPPROVAL.getStatus(), "vacation");
        
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
        leaveRequests.add(new LeaveRequest().setStatus(Status.APPROVEDMANAGER.getStatus()).setId(2L));
        Mockito.when(leaveRequestService.getAllLeaveRequests(new PageRequest(0,10)))
                .thenReturn(new PageImpl<LeaveRequest>(leaveRequests, new PageRequest(0,10), 100));
    
        mockMvc.perform(get("/api/leaverequest").param("page","0").param("size","10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content.size()", is(1)))
                .andExpect(jsonPath("size", is(10)))
                .andExpect(jsonPath("content[0].status", is(Status.APPROVEDMANAGER.getStatus())))
                .andExpect(jsonPath("content[0].id", is(2)));
    }
    
    @Test
    public void getAllLeaveRequestsByStatus() throws Exception
    {
        //Given
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        leaveRequests.add(new LeaveRequest().setStatus(Status.WAITINGAPPROVAL.getStatus()).setId(2L));
        Mockito.when(leaveRequestService.getAllLeaveRequestsByStatus(Status.WAITINGAPPROVAL.getStatus(), new PageRequest(0,10)))
                .thenReturn(new PageImpl<LeaveRequest>(leaveRequests, new PageRequest(0,10), 100));
        
        mockMvc.perform(get("/api/leaverequest/waiting").param("page","0").param("size","10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content.size()", is(1)))
                .andExpect(jsonPath("size", is(10)))
                .andExpect(jsonPath("content[0].status", is(Status.WAITINGAPPROVAL.getStatus())))
                .andExpect(jsonPath("content[0].id", is(2)));
    }
    
    @Test
    public void getAllLeaveRequestsByPersonId() throws Exception
    {
        //Given
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        leaveRequests.add(new LeaveRequest().setStatus(Status.APPROVEDMANAGER.getStatus()).setPersonId(2L));
        leaveRequests.add(new LeaveRequest().setStatus(Status.REJECTED.getStatus()).setPersonId(2L));
        Mockito.when(leaveRequestService.getAllLeaveRequestsByPersonId(2L, new PageRequest(0, 10)))
                .thenReturn(new PageImpl<LeaveRequest>(leaveRequests, new PageRequest(0,10), 100));
    
        mockMvc.perform(get("/api/leaverequest/person/2").param("page","0").param("size","10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content.size()", is(2)))
                .andExpect(jsonPath("size", is(10)))
                .andExpect(jsonPath("content[0].status", is(Status.APPROVEDMANAGER.getStatus())))
                .andExpect(jsonPath("content[0].personId", is(2)))
                .andExpect(jsonPath("content[1].status", is(Status.REJECTED.getStatus())))
                .andExpect(jsonPath("content[1].personId", is(2)));
    }
    
    @Test
    public void getLeaveRequestById() throws Exception
    {
        //Given
        LeaveRequest returnedLeaveRequest = new LeaveRequest().setId(1L).setStatus(Status.APPROVEDMANAGER.getStatus());
        Mockito.when(leaveRequestService.getLeaveRequestById(1L)).thenReturn(returnedLeaveRequest);
        
        mockMvc.perform(get("/api/leaverequest/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Status.APPROVEDMANAGER.getStatus())));
    }
    
    @Test
    public void updateLeaveRequestStatusApproved() throws Exception
    {
        //Given
        LeaveRequest returnedLeaveRequest = new LeaveRequest().setId(1L).setStatus(Status.APPROVEDMANAGER.getStatus());
        Mockito.when(leaveRequestService.updateLeaveRequestApprovedByManager(1L)).thenReturn(returnedLeaveRequest);
    
        mockMvc.perform(put("/api/leaverequest/1/changestatus/approved/manager"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Status.APPROVEDMANAGER.getStatus())));
    }
    
    @Test
    public void updateLeaveRequestStatusRejected() throws Exception
    {
        //Given
        LeaveRequest returnedLeaveRequest = new LeaveRequest().setId(1L).setStatus(Status.REJECTED.getStatus());
        Mockito.when(leaveRequestService.updateLeaveRequestRejected(1L)).thenReturn(returnedLeaveRequest);
        
        mockMvc.perform(put("/api/leaverequest/1/changestatus/rejected"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Status.REJECTED.getStatus())));
    }
    
    @Test
    public void  getAllTypesAbsence() throws Exception
    {
        //Given
        String[] types = TypesAbsence.typesAbsence();
        Mockito.when(leaveRequestService.getAllTypesAbsence()).thenReturn(types);
    
        mockMvc.perform(get("/api/leaverequest/typesabsence"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }
    
    @Test
    @WithMockUser(username = "user", authorities = "ADMIN")
    public void testSecurity() throws Exception
    {
        //Given
        LeaveRequest returnedLeaveRequest = new LeaveRequest().setId(1L).setStatus(Status.APPROVEDMANAGER.getStatus());
        Mockito.when(leaveRequestService.updateLeaveRequestApprovedByManager(1L)).thenReturn(returnedLeaveRequest);
    
        mockMvc.perform(put("/api/leaverequest/1/changestatus/approved/manager"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Status.APPROVEDMANAGER.getStatus())));
    }
}