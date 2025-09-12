package com.example.placement.controller;

import com.example.placement.dto.DriveDTO;
import com.example.placement.entity.Application;
import com.example.placement.entity.Drive;
import com.example.placement.entity.Student;
import com.example.placement.entity.User;
import com.example.placement.repository.ApplicationRepository;
import com.example.placement.service.DriveService;
import com.example.placement.service.StudentService;
import com.example.placement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/drives")
public class DriveController {

    @Autowired private DriveService driveService;
    @Autowired private UserService userService;
    @Autowired private StudentService studentService;
    @Autowired private ApplicationRepository applicationRepository;

    @GetMapping
    public ResponseEntity<List<DriveDTO>> list() {
        List<DriveDTO> list = driveService.listAll().stream()
                .map(d -> new DriveDTO(d.getId(), d.getCompanyName(), d.getJd(), d.getDateOfDrive(), d.getLocation()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<DriveDTO> createDrive(@RequestBody DriveDTO dto, java.security.Principal principal) {
        Drive d = new Drive();
        d.setCompanyName(dto.getCompanyName());
        d.setJd(dto.getJd());
        d.setDateOfDrive(dto.getDateOfDrive());
        d.setLocation(dto.getLocation());
        // createdBy = current user id
        User u = userService.findByUsername(principal.getName());
        d.setCreatedBy(u.getId());
        Drive saved = driveService.create(d);
        dto.setId(saved.getId());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{driveId}/apply")
    public ResponseEntity<?> apply(@PathVariable Long driveId, java.security.Principal principal) {
        User u = userService.findByUsername(principal.getName());
        Student s = studentService.findByUser(u);
        Drive d = driveService.findById(driveId);

        Application application = new Application();
        application.setStudent(s);
        application.setDrive(d);
        application.setStatus("APPLIED");
        application.setAppliedAt(OffsetDateTime.now());
        applicationRepository.save(application);
        return ResponseEntity.ok(Map.of("status","applied"));
    }
}
