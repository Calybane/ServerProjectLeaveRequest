package com.example.leaverequest.repository;

import com.example.leaverequest.LeaverequestprojectApplication;
import com.example.leaverequest.model.LeaveRequest;
import com.example.leaverequest.model.Status;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import javafx.application.Application;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.List;

import static org.junit.Assert.*;

@TestExecutionListeners({DbUnitTestExecutionListener.class})
@SpringBootTest(classes = LeaverequestprojectApplication.class)
@DirtiesContext
@ActiveProfiles("test")
@DatabaseSetup("classpath:datasets/leaveRequestData.xml")
@DatabaseTearDown(type = DatabaseOperation.CLEAN_INSERT)
public class LeaveRequestRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests
{
    @Autowired
    LeaveRequestRepository leaveRequestRepository;
    
    @Test
    public void findAllByPersonIdOrderByStatusAscLeaveFromAsc() throws Exception
    {
        List<LeaveRequest> all = leaveRequestRepository.findAllByPersonIdOrderByStatusAscLeaveFromAsc(1L);
        Assert.assertEquals(all.size(), 1);
        Assert.assertEquals(all.get(0).getPersonId(), 1L);
    }
    
    @Test
    public void findAllByStatusLike() throws Exception
    {
        List<LeaveRequest> all = leaveRequestRepository.findAllByStatusLike(Status.APPROVED, new Sort(Sort.Direction.ASC, "personId"));
        Assert.assertEquals(all.size(), 2);
        Assert.assertEquals(all.get(0).getStatus(), Status.APPROVED);
        Assert.assertEquals(all.get(1).getStatus(), Status.APPROVED);
        Assert.assertNotSame(all.get(0).getPersonId(), all.get(1).getPersonId());
    }
    
}