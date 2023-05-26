package com.cra.portfolio.model;

import com.cra.portfolio.enums.Env;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "environments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Environment {


        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Integer id;
        private LocalDateTime deletedAt = null;
        private LocalDateTime modifiedAt = null;
        private LocalDateTime createdAt = null;
        private String environmentName;
        private Env type;
        private String description;
        @JsonIgnore
        @OneToMany(mappedBy = "environment", fetch = FetchType.EAGER)
        private List<Server> servers = new ArrayList<>();

        /*@JsonIgnore
        @ManyToOne
        @JoinColumn(name = "dataCenterId")
        private DataCenter dataCenter;

        @OneToMany(mappedBy = "environment", fetch = FetchType.EAGER)
        private List<Server> servers = new ArrayList<>();*/

    }

