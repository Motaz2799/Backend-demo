package com.cra.portfolio.dto;

import com.cra.portfolio.model.Application;
import com.cra.portfolio.model.DataCenter;
import com.cra.portfolio.model.Database;
import com.cra.portfolio.model.Environment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServerResponse {
    private Integer id;
    private String serverName;
    private String dataSource;
    private String type;
    private LocalDateTime deletedAt;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
    private String role;
    private Integer currentNumberOfCores;
    private Integer currentRamGb;
    private Integer currentDiskGb;
    private String powerStatus;
    private String serverNotes;
    private String ipAddress;
    private String operatingSystem;
    private List<Application> applications;
    private List<Database> databaseList;
    private Environment environment;
    private DataCenter datacenter;
}
