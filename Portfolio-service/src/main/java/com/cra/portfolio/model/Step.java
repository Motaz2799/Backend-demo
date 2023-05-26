package com.cra.portfolio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "Steps")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String step ;
    @OneToMany(mappedBy="step",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Category> categories ;
    private LocalDateTime deletedAt = null ;
    private LocalDateTime modifiedAt = null ;
    private LocalDateTime createdAt = null ;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;
}
