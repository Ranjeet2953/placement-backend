package com.example.placement.controller;

import com.example.placement.repository.ApplicationRepository;
import com.example.placement.repository.UserRepository;
import com.example.placement.dto.ApplicationDTO;
import com.example.placement.entity.Application;
import com.example.placement.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

   @Autowired private ApplicationRepository applicationRepository;
      @Autowired private UserRepository userRepository;

      @GetMapping("/applications")
      public List<Application> getApplications() {
          return applicationRepository.findAll();
      }
      @GetMapping("/drives/{driveId}/applications")
      public List<ApplicationDTO> getApplicationsByDrive(@PathVariable Long driveId) {
          List<Application> applications = applicationRepository.findByDriveId(driveId);
          return applications.stream().map(app -> new ApplicationDTO(
              app.getId(),
              app.getStudent() != null ? app.getStudent().getFullName() : "Unknown",
              app.getStatus(),
              app.getAppliedAt()
          )).collect(Collectors.toList());
      }



      @GetMapping("/users")
      public List<User> getUsers() {
          return userRepository.findAll();
      }
}