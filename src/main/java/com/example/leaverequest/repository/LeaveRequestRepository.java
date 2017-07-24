package com.example.leaverequest.repository;

import com.example.leaverequest.model.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long>
{
    List<LeaveRequest> findAllByPersonId(long personId);
    
    List<LeaveRequest> findAllByStatusLike(String status, Sort sort);
    
    List<LeaveRequest> findAllByStatusLikeAndLeaveFromAfter(String status, Date leaveFrom, Sort sort);
}
