package com.cra.portfolio.model;

import com.cra.portfolio.enums.Flow;
import com.cra.portfolio.enums.Frequency;
import com.cra.portfolio.enums.ProcessingMode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications_interface")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationsInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "applicationSrc_id")
    private Application applicationSrc;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "applicationTarget_id")
    private Application applicationTarget;
    private LocalDateTime deletedAt = null;
    private LocalDateTime modifiedAt = null;
    private LocalDateTime createdAt = null;

    private String protocol;
    private String dataFormat;
    private String notes;
    private Flow flow;
    private Frequency frequency;
    private ProcessingMode processingMode;


}
