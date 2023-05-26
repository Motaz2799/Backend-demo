package com.cra.portfolio.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "os")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class OperatingSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String OsName;
    private String OsVersion;


}
