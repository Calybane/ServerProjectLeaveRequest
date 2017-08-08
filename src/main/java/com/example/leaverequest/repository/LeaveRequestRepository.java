package com.example.leaverequest.repository;

import com.example.leaverequest.model.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long>
{
    Page<LeaveRequest> findAllByLogin(String login, Pageable pageable);
    
    Page<LeaveRequest> findAllByStatusLike(String status, Pageable pageable);
    
    List<LeaveRequest> findAllByLoginAndLeaveFromAfter(String login, Date date);
    
    List<LeaveRequest> findAllByStatusNotLike(String status);
    
    @Transactional
    @Modifying
    @Query(value = "UPDATE LEAVE_REQUEST set DAYS_TAKEN = DAYS_TAKEN + 1 " +
                    "WHERE LEAVE_FROM <= :holiday and LEAVE_TO >= :holiday",
                    nativeQuery = true)
    void addOneDayTaken(@Param("holiday") Date date);
    
    @Transactional
    @Modifying
    @Query(value = "UPDATE LEAVE_REQUEST set DAYS_TAKEN = DAYS_TAKEN - 1 " +
                    "WHERE LEAVE_FROM <= :holiday and LEAVE_TO >= :holiday",
                    nativeQuery = true)
    void removeOneDayTaken(@Param("holiday") Date date);
}
