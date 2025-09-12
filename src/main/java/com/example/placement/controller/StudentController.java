package com.example.placement.controller;
import com.example.placement.dto.StudentDTO;
import com.example.placement.entity.Application;
import com.example.placement.entity.Student;
import com.example.placement.entity.User;
import com.example.placement.repository.ApplicationRepository;
import com.example.placement.service.StudentService;
import com.example.placement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired private StudentService studentService;
    @Autowired private UserService userService;
    @Autowired
    private ApplicationRepository applicationRepository;

    @Value("${placement.upload.dir}")
    private String uploadDir;

    @GetMapping("/profile")
    public ResponseEntity<StudentDTO> getProfile(Principal principal) {
        User u = userService.findByUsername(principal.getName());
        Student s = studentService.findByUser(u);
        StudentDTO dto = new StudentDTO(s.getId(), u.getUsername(), s.getFullName(), s.getEmail(),
                s.getPhone(), s.getResumePath(), s.getBatch(), s.getCollege());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/profile")
    public ResponseEntity<StudentDTO> updateProfile(Principal principal, @RequestBody StudentDTO dto) {
        User u = userService.findByUsername(principal.getName());
        Student s = studentService.findByUser(u);
        s.setFullName(dto.getFullName());
        s.setEmail(dto.getEmail());
        s.setPhone(dto.getPhone());
        s.setBatch(dto.getBatch());
        s.setCollege(dto.getCollege());
        studentService.update(s);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/profile/resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadResume(Principal principal, @RequestParam("file") MultipartFile file) throws Exception {
        User u = userService.findByUsername(principal.getName());
        Student s = studentService.findByUser(u);

        Path dir = Paths.get(uploadDir);
        if (!Files.exists(dir)) Files.createDirectories(dir);
        String filename = "student_" + s.getId() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path target = dir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        studentService.updateResumePath(s.getId(), target.toAbsolutePath().toString());
        return ResponseEntity.ok().body(Map.of("path", target.toAbsolutePath().toString()));
    }
    @GetMapping("/applications")
    public ResponseEntity<?> getMyApplications(java.security.Principal principal) {
        User u = userService.findByUsername(principal.getName());
        Student s = studentService.findByUser(u);
        List<Application> applications = applicationRepository.findByStudent(s);
        // Optionally map to DTO if needed
        return ResponseEntity.ok(applications);
    }
    
  
}
