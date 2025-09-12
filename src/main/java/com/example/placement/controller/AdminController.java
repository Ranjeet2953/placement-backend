package com.example.placement.controller;

import com.example.placement.repository.ApplicationRepository;
import com.example.placement.entity.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired private ApplicationRepository applicationRepository;

    @PutMapping("/applications/{id}/status")
    public ResponseEntity<?> changeStatus(@PathVariable Long id, @RequestBody Map<String,String> body) {
        String status = body.get("status");
        Application a = applicationRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        a.setStatus(status);
        applicationRepository.save(a);
        return ResponseEntity.ok(Map.of("id", id, "status", status));
    }
}
