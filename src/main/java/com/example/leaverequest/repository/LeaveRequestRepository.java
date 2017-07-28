package com.example.leaverequest.repository;

import com.example.leaverequest.model.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long>
{
    Page<LeaveRequest> findAllByPersonId(long personId, Pageable pageable);
    
    Page<LeaveRequest> findAllByStatusLike(String status, Pageable pageable);
}
