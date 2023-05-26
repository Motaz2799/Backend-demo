package com.cra.portfolio.dto;


import com.cra.portfolio.model.Contact;
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

public class ApplicationResponse {
    private Integer id;
    private String appName;
    private String appDescription;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<Server> servers;
    private List<Contact> contacts;

}
