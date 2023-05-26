package com.cra.portfolio.dto;

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
public class DatabaseResponse {
    private Integer id;
    private LocalDateTime deletedAt = null;
    private LocalDateTime modifiedAt = null;
    private LocalDateTime createdAt = null;
    private String nameDb;
    private String versionDb;
    private List<Server> serverList;
}
