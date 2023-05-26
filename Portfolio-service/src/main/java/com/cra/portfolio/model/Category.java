package com.cra.portfolio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "Categories")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String category ;

    private LocalDateTime createdAt = null ;

    private LocalDateTime deletedAt = null ;
    private LocalDateTime modifiedAt = null ;


    @OneToMany(mappedBy="category",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Question> questions;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "step_id")
    private Step step;

}
