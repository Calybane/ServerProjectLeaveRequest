package com.example.leaverequest.repository;

import com.example.leaverequest.model.LeaveRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long>
{
    List<LeaveRequest> findAllByPersonIdOrderByStatusAscLeaveFromAsc(long personId);
    
    List<LeaveRequest> findAllByStatusLike(@Param("status") String status, Sort sort);
}
