package com.example.placement.repository;

import com.example.placement.entity.Application;
import com.example.placement.entity.Student;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
	List<Application> findByStudent(Student student);
	List<Application> findByDriveId(Long driveId);

}
