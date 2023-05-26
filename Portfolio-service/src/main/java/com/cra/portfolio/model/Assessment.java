package com.cra.portfolio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "assessments")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String assessment ;
    private LocalDateTime deletedAt = null ;
    private LocalDateTime modifiedAt = null ;
    private LocalDateTime createdAt = null ;
    @JsonIgnore

    @OneToMany(mappedBy="assessment",cascade = CascadeType.ALL)
    private List<Application> applications ;
    private String note ;

    @OneToMany(mappedBy="assessment",fetch = FetchType.EAGER)
    private List<Step> steps ;

}
