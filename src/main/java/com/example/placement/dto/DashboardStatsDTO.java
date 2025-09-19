package com.example.placement.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {

    private int availableDrives;
    private int applicationsCount;
    private int acceptedCount;
    private int pendingCount;
    
    private int totalApplications;
    private int interviewCount;

    private List<DriveDTO> recentDrives;
    private List<ApplicationStatusDTO> applicationStatus;
    private List<ApplicationDTO> applicationsList;
    
}