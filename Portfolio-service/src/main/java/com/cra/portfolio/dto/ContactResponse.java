package com.cra.portfolio.dto;

import com.cra.portfolio.model.Application;
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
public class ContactResponse {
    private Integer id;
    private LocalDateTime deletedAt = null;
    private LocalDateTime modifiedAt = null;
    private LocalDateTime createdAt = null;
    private String fullName;
    private String title;
    private String department;
    private String email;
    private List<Application> applications;

}
