package com.cra.portfolio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BaseDeDonnee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class Database {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "database_id")
    private Integer databaseId;
    @Column(name = "database_name")

    private String databaseName;
    @Column(name = "database_version")

    private String databaseVersion;
    private LocalDateTime deletedAt = null;
    private LocalDateTime modifiedAt = null;
    private LocalDateTime createdAt = null;
    @ManyToMany(mappedBy = "databaseList", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Server> serverList = new ArrayList<>();


    public void addServer(Server server) {
        server.getDatabaseList().add(this);
        this.getServerList().add(server);
    }

    public void removeServer(Server server) {
        server.getDatabaseList().remove(this);
        this.getServerList().remove(server);
    }

}
