package com.example.leaverequest.service;

import com.example.leaverequest.Utils;
import com.example.leaverequest.dto.LeaveRequestDTO;
import com.example.leaverequest.exception.EntityBadInformationsException;
import com.example.leaverequest.exception.EntityNotFoundException;
import com.example.leaverequest.model.LeaveRequest;
import com.example.leaverequest.model.Status;
import com.example.leaverequest.model.TypesAbsence;
import com.example.leaverequest.model.User;
import com.example.leaverequest.repository.HolidayDateRepository;
import com.example.leaverequest.repository.LeaveRequestRepository;
import com.example.leaverequest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService
{
    @Autowired
    LeaveRequestRepository leaveRequestRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    HolidayDateRepository holidayDateRepository;
    
    
    // Not used
    @Override
    @PreAuthorize("isAuthenticated()")
    public List<LeaveRequest> getAllLeaveRequests()
    {
        return leaveRequestRepository.findAll();
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public List<LeaveRequest> getAllLeaveRequestsNotRejected()
    {
        return leaveRequestRepository.findAllByStatusNotLike(Status.REJECTED.getStatus());
    }
    
    @Override
    @PreAuthorize("isAuthenticated()")
    public Page<LeaveRequest> getAllLeaveRequests(Pageable pageable)
    {
        return leaveRequestRepository.findAll(pageable);
    }
    
    @Override
    @PreAuthorize("isAuthenticated()")
    public Page<LeaveRequest> getAllLeaveRequestsByLogin(String login, Pageable pageable)
    {
        return leaveRequestRepository.findAllByLogin(login, pageable);
    }
    
    @Override
    @PreAuthorize("isAuthenticated()")
    public List<Date> getAllDisabledDatesByLogin(String login)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -2);
        
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findAllByLoginAndLeaveFromAfter(login, c.getTime());
        List<Date> dates = new ArrayList<>();
        for (LeaveRequest request : leaveRequests) {
            Date start = request.getLeaveFrom();
            Date end = request.getLeaveTo();
            while (start.compareTo(end) <= 0) {
                dates.add(start);
                c.setTime(start);
                c.add(Calendar.DAY_OF_MONTH, 1);
                start = c.getTime();
            }
        }
        
        return dates;
    }
    
    @Override
    @PreAuthorize("isAuthenticated()")
    public Page<LeaveRequest> getAllLeaveRequestsByStatus(String status, Pageable pageable)
    {
        return leaveRequestRepository.findAllByStatusLike(status, pageable);
    }
    
    @Override
    @PreAuthorize("isAuthenticated()")
    public LeaveRequest getLeaveRequestById(long id)
    {
        return leaveRequestRepository.findOne(id);
    }
    
    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public LeaveRequest createLeaveRequest(LeaveRequestDTO dto)
    {
        LeaveRequest leaveRequest = new LeaveRequest(dto);
        leaveRequest.setStatus(Status.WAITINGAPPROVAL.getStatus())
                .setApprovalManagerDate(null)
                .setApprovalHRDate(null)
                .setLeaveFrom(Utils.getZeroTimeDate(leaveRequest.getLeaveFrom()))
                .setLeaveTo(Utils.getZeroTimeDate(leaveRequest.getLeaveTo()))
                .setRequestDate(Utils.getZeroTimeDate(new Date()));
    
        int nbDays = 0;
        Calendar c = Calendar.getInstance();
        Date start = leaveRequest.getLeaveFrom();
        Date end = leaveRequest.getLeaveTo();
        while (start.compareTo(end) <= 0) {
            c.setTime(start);
            if (c.get(Calendar.DAY_OF_WEEK) >= Calendar.MONDAY && c.get(Calendar.DAY_OF_WEEK) <= Calendar.FRIDAY ) {
                ++nbDays;
            }
            c.add(Calendar.DAY_OF_MONTH, 1);
            start = c.getTime();
        }
        leaveRequest.setDaysTaken(nbDays);
        
        User user = userRepository.findByLogin(leaveRequest.getLogin());
        
        if ((user.getDaysLeft() - leaveRequest.getDaysTaken()) < 0) {
            throw new EntityBadInformationsException("Don't have enough days left");
        } else if (!leaveRequest.valid(getAllDisabledDatesByLogin(leaveRequest.getLogin()))) {
            throw new EntityBadInformationsException("Leave request informations are incorrect");
        } else {
            System.out.println("Creating leaverequest : " + leaveRequest.toString());
            int nbHolidays = holidayDateRepository.countAllByDateBetween(leaveRequest.getLeaveFrom(), leaveRequest.getLeaveTo());
            leaveRequest.setDaysTaken(leaveRequest.getDaysTaken() - nbHolidays);
            userRepository.save(user.setDaysLeft(user.getDaysLeft() - leaveRequest.getDaysTaken()));
            return leaveRequestRepository.save(leaveRequest);
        }
    }
    
    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public LeaveRequest updateLeaveRequestApprovedByManager(long id)
    {
        LeaveRequest leaveRequest = leaveRequestRepository.findOne(id);
        if(leaveRequest == null) {
            throw new EntityNotFoundException("Leave request with id " + id + " not found.");
        } else if (!leaveRequest.getStatus().equals(Status.WAITINGAPPROVAL.getStatus())) {
            throw new EntityBadInformationsException("This leave request is not in '" + Status.WAITINGAPPROVAL.getStatus() + "'");
        } else if (SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                    .stream().noneMatch(role -> role.getAuthority().toLowerCase().equals("manager"))) {
            throw new EntityBadInformationsException("The user is not an manager");
        } else {
            leaveRequest.setStatus(Status.APPROVEDMANAGER.getStatus());
            leaveRequest.setApprovalManagerDate(new Date());
            
            System.out.println("Updating leaverequest : id=" + leaveRequest.getId() + ", status='" + leaveRequest.getStatus() + "'");
            return leaveRequestRepository.save(leaveRequest);
        }
    }
    
    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HR')")
    public LeaveRequest updateLeaveRequestApprovedByHR(long id)
    {
        LeaveRequest leaveRequest = leaveRequestRepository.findOne(id);
        if(leaveRequest == null) {
            throw new EntityNotFoundException("Leave request with id " + id + " not found");
        } else if (!leaveRequest.getStatus().equals(Status.APPROVEDMANAGER.getStatus())) {
            throw new EntityBadInformationsException("This leave request is not in '" + Status.APPROVEDMANAGER.getStatus() + "'");
        } else if (SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                    .stream().noneMatch(role -> role.getAuthority().toLowerCase().equals("hr"))) {
            throw new EntityBadInformationsException("The user is not an HR");
        } else {
            leaveRequest.setStatus(Status.APPROVEDHR.getStatus());
            leaveRequest.setApprovalHRDate(new Date());
    
            System.out.println("Updating leaverequest : id=" + leaveRequest.getId() + ", status='" + leaveRequest.getStatus() + "'");
            return leaveRequestRepository.save(leaveRequest);
        }
    }
    
    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HR')")
    public LeaveRequest updateLeaveRequestRejected(long id)
    {
        LeaveRequest leaveRequest = leaveRequestRepository.findOne(id);
        if(leaveRequest == null) {
            throw new EntityNotFoundException("Leave request with id " + id + " not found");
        } else {
            leaveRequest.setStatus(Status.REJECTED.getStatus());
            if(leaveRequest.getApprovalManagerDate() == null) leaveRequest.setApprovalManagerDate(new Date());
            else leaveRequest.setApprovalHRDate(new Date());
    
            System.out.println("Updating leaverequest : id=" + leaveRequest.getId() + ", status='" + leaveRequest.getStatus() + "'");
            userRepository.addDays(leaveRequest.getDaysTaken(), leaveRequest.getLogin());
            return leaveRequestRepository.save(leaveRequest);
        }
    }
    
    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public boolean deleteLeaveRequest(long id)
    {
        LeaveRequest leaveRequest = leaveRequestRepository.findOne(id);
        if (leaveRequest == null) {
            throw new EntityNotFoundException("Leave request with id " + id + " not found");
        } else if (!leaveRequest.getStatus().equals(Status.WAITINGAPPROVAL.getStatus())) {
            throw new EntityBadInformationsException("This leave request is already in approbation or rejected");
        } else {
            System.out.println("Deleting leaverequest : id=" + leaveRequest.getId());
            leaveRequestRepository.delete(id);
            userRepository.addDays(leaveRequest.getDaysTaken(), leaveRequest.getLogin());
            return leaveRequestRepository.findOne(id) == null;
        }
    }
    
    @Override
    @PreAuthorize("isAuthenticated()")
    public String[] getAllTypesAbsence()
    {
        return TypesAbsence.typesAbsence();
    }
}
