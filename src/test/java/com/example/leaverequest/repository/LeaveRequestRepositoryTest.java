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

import java.lang.invoke.LambdaConversionException;
import java.util.Date;
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
    public void findAll() throws Exception
    {
        List<LeaveRequest> all = leaveRequestRepository.findAll(new Sort(Sort.Direction.ASC, "leaveFrom"));
        Assert.assertEquals(all.size(), 5);
        Assert.assertTrue("First date (" + all.get(0).getLeaveFrom() + ") should be lower or equals than second (" +
                all.get(1).getLeaveFrom() + ")", all.get(0).getLeaveFrom().compareTo(all.get(1).getLeaveFrom()) <= 0);
        Assert.assertTrue("First date (" + all.get(0).getLeaveFrom() + ") should be lower or equals than second (" +
                all.get(1).getLeaveFrom() + ")", all.get(1).getLeaveFrom().compareTo(all.get(2).getLeaveFrom()) <= 0);
    }
    
    @Test
    public void findAllByPersonId() throws Exception
    {
        List<LeaveRequest> all = leaveRequestRepository.findAllByPersonId(1L);
        Assert.assertEquals(all.size(), 1);
        Assert.assertEquals(all.get(0).getPersonId(), 1L);
    }
    
    @Test
    public void findAllByStatusLike() throws Exception
    {
        List<LeaveRequest> all = leaveRequestRepository.findAllByStatusLike(Status.WAITINGAPPROVAL, new Sort(Sort.Direction.ASC, "leaveFrom"));
        Assert.assertEquals(all.size(), 3);
        Assert.assertEquals(all.get(0).getStatus(), Status.WAITINGAPPROVAL);
        Assert.assertEquals(all.get(1).getStatus(), Status.WAITINGAPPROVAL);
        Assert.assertEquals(all.get(2).getStatus(), Status.WAITINGAPPROVAL);
        Assert.assertTrue("First date (" + all.get(0).getLeaveFrom() + ") should be lower or equals than second (" +
                all.get(1).getLeaveFrom() + ")", all.get(0).getLeaveFrom().compareTo(all.get(1).getLeaveFrom()) <= 0);
    }
    
    @Test
    public void findAllByStatusLikeAndLeaveFromAfter() throws Exception
    {
        List<LeaveRequest> all = leaveRequestRepository.findAllByStatusLikeAndLeaveFromAfter(Status.WAITINGAPPROVAL, new Date(), new Sort(Sort.Direction.ASC, "leaveFrom"));
        Assert.assertEquals(all.size(), 2);
        Assert.assertEquals(all.get(0).getStatus(), Status.WAITINGAPPROVAL);
        Assert.assertEquals(all.get(1).getStatus(), Status.WAITINGAPPROVAL);
        Assert.assertTrue("First date (" + all.get(0).getLeaveFrom() + ") should be greather than today (" +
                new Date() + ")", all.get(0).getLeaveFrom().after(new Date()));
        Assert.assertTrue("First date (" + all.get(0).getLeaveFrom() + ") should be lower or equals than second (" +
                all.get(1).getLeaveFrom() + ")", all.get(0).getLeaveFrom().compareTo(all.get(1).getLeaveFrom()) <= 0);
    }
    
}