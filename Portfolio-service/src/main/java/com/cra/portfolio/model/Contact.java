package com.cra.portfolio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Contacts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private LocalDateTime deletedAt = null;
    private LocalDateTime modifiedAt = null;
    private LocalDateTime createdAt = null;
    private String fullName;
    private String title;
    private String department;
    private String email;
    @ManyToMany(mappedBy = "contacts", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Application> applications = new ArrayList<>();


    public void addApplication(Application application) {
        this.applications.add(application);
        application.getContacts().add(this);
    }

    public void removeApplication(Application application) {
        this.applications.remove(application);
        application.getContacts().remove(this);
    }


}
