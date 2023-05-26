package com.cra.portfolio.dto;

import com.cra.portfolio.enums.Env;
import com.cra.portfolio.model.DataCenter;
import com.cra.portfolio.model.Server;
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
public class EnvironmentResponse {
    private Integer id;
    private LocalDateTime deletedAt = null;
    private LocalDateTime modifiedAt = null;
    private LocalDateTime createdAt = null;
    private String environmentName;
    private String description;
    private Env type;
    private List<Server> servers;
}
