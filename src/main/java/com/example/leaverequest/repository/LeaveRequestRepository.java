package com.example.leaverequest.repository;

import com.example.leaverequest.model.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long>
{
    Page<LeaveRequest> findAllByLogin(String login, Pageable pageable);
    
    Page<LeaveRequest> findAllByStatusLike(String status, Pageable pageable);
    
    List<LeaveRequest> findAllByLoginAndLeaveFromIsAfter(String login, Date date);
    
    List<LeaveRequest> findAllByStatusNotLike(String status);
}
