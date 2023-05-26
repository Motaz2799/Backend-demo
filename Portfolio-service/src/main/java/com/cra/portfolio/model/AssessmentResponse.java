package com.cra.portfolio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Table(name = "AssessmentResponse")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String response;

    private Integer appId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    private Question question;
    private LocalDateTime deletedAt = null;
    private LocalDateTime modifiedAt = null;
    private LocalDateTime createdAt = null;
}
