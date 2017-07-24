package com.example.leaverequest;

import com.example.leaverequest.model.LeaveRequest;
import com.example.leaverequest.repository.LeaveRequestRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;


@SpringBootApplication
public class LeaverequestprojectApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(LeaverequestprojectApplication.class, args);
	}
}
