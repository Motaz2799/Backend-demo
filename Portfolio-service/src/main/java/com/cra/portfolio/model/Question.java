package com.cra.portfolio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Questions")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Boolean required = true ;
    private String question ;
    private LocalDateTime deletedAt = null ;
    private LocalDateTime modifiedAt = null ;
    private LocalDateTime createdAt = null ;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    private Category category ;
    public enum type{checkbox_group,number,paragraph,radio_group,select,text,textarea} ;
    @Enumerated(EnumType.STRING)
    private type type;
    @OneToMany(mappedBy="question",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Option> options;

    private String response ;

}
