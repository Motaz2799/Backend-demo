package com.cra.portfolio.dto;

import com.cra.portfolio.model.Environment;
import com.cra.portfolio.model.Server;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataCenterRequest {
    private String name;
    private String notes;
    private String location;
    private LocalDateTime contractExpirationDate;
    private LocalDateTime deletedAt = null;
    private LocalDateTime modifiedAt = null;
    private LocalDateTime createdAt = null;
    private List<Server> servers;
}
