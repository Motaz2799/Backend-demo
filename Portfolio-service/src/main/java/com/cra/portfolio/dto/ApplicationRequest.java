package com.cra.portfolio.dto;

import com.cra.portfolio.model.Contact;
import com.cra.portfolio.model.Server;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRequest {
    private String appName;
    private String appDescription;
    private LocalDateTime deletedAt = null;
    private LocalDateTime createdAt = null;
    private LocalDateTime modifiedAt = null;
    private List<Server> servers;

    private List<Contact> contacts;


}
