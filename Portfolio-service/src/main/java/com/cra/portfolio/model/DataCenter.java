package com.cra.portfolio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DataCenters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    private String notes;
    private String location;
    private LocalDateTime contractExpirationDate;
    private LocalDateTime deletedAt = null;
    private LocalDateTime modifiedAt = null;
    private LocalDateTime createdAt = null;
@JsonIgnore
    @OneToMany(mappedBy = "datacenter", fetch = FetchType.EAGER)
    private List<Server> servers = new ArrayList<>();
    /*@JsonIgnore
    @OneToMany(mappedBy = "dataCenter", fetch = FetchType.EAGER)
    private List<Environment> environments = new ArrayList<>();*/

}
