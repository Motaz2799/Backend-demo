package com.cra.portfolio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "servers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private LocalDateTime deletedAt = null;
    private LocalDateTime modifiedAt = null;
    private LocalDateTime createdAt = null;

    private String serverName;
    private String dataSource;
    private String type;
    private String role;
    private Integer currentNumberOfCores;
    private Integer currentRamGb;
    private Integer currentDiskGb;
    private String powerStatus;
    private String serverNotes;
    private String ipAddress;

    private String operatingSystem;

    /*@ManyToOne
    @JoinColumn(name = "environmentId")
    private Environment environment;*/
    @ManyToOne
    @JoinColumn(name = "dataCenterId")
    private DataCenter datacenter;

    @ManyToOne
    @JoinColumn(name = "environmentId")
    private Environment environment;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "server_database",
            joinColumns = @JoinColumn(name = "server_id"),
            inverseJoinColumns = @JoinColumn(name = "database_id"))
    private List<Database> databaseList = new ArrayList<>();


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(name = "server_application",
            joinColumns = @JoinColumn(name = "server_id"),
            inverseJoinColumns = @JoinColumn(name = "application_id"))
    private List<Application> applications = new ArrayList<>();


    public void addDatabase(Database database) {
        database.getServerList().add(this);
        this.getDatabaseList().add(database);
    }

    public void removeDatabase(Database database) {
        database.getServerList().remove(this);
        this.getDatabaseList().remove(database);
    }


}



