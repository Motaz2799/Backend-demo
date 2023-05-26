package com.cra.portfolio.service;

import com.cra.portfolio.dto.ServerRequest;
import com.cra.portfolio.dto.ServerResponse;
import com.cra.portfolio.exception.NotFoundCustomException;
import com.cra.portfolio.model.*;
import com.cra.portfolio.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServerService {
    private final ApplicationRepository applicationRepository;
    private final ServerRepository serverRepository;
    private final DatabaseRepository databaseRepository;
    private final DataCenterRepository dataCenterRepository;
    private final EnvironmentRepository environmentRepository;

    public ServerResponse createServer(ServerRequest serverRequest) {
        LocalDateTime created = LocalDateTime.now();
        Server server = Server.builder()
                .serverName(serverRequest.getServerName())
                .dataSource(serverRequest.getDataSource())
                .type(serverRequest.getType())
                .role(serverRequest.getRole())
                .currentNumberOfCores(serverRequest.getCurrentNumberOfCores())
                .currentRamGb(serverRequest.getCurrentRamGb())
                .currentDiskGb(serverRequest.getCurrentDiskGb())
                .powerStatus(serverRequest.getPowerStatus())
                .serverNotes(serverRequest.getServerNotes())
                .ipAddress(serverRequest.getIpAddress())
                .operatingSystem(serverRequest.getOperatingSystem())
                .createdAt(created)

                .build();
        serverRepository.save(server);

        log.info("server {} created successfully", server.getId());
        return mapToServerResponse(server);
    }


    public ServerResponse addAppToServer(Integer serverId, Integer applicationId, ServerRequest serverRequest) {
        Optional<Server> optionalServer = serverRepository.findById(serverId);
        if (optionalServer.isPresent()) {
            Server server = optionalServer.get();
            Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new NotFoundCustomException("Application not found with id: " + applicationId));
            if (application.getDeletedAt() != null) {
                throw new NotFoundCustomException("Application not found with id: " + applicationId);
            }
            List<Application> applications = server.getApplications();
            if (!applications.contains(application)) {
                applications.add(application);
                application.getServers().add(server);
                server.setApplications(applications);
                server.setServerName(serverRequest.getServerName());
                server.setDataSource(serverRequest.getDataSource());
                server.setType(serverRequest.getType());
                server.setRole(serverRequest.getRole());
                server.setCurrentNumberOfCores(serverRequest.getCurrentNumberOfCores());
                server.setCurrentRamGb(serverRequest.getCurrentRamGb());
                server.setCurrentDiskGb(serverRequest.getCurrentDiskGb());
                server.setPowerStatus(serverRequest.getPowerStatus());
                server.setServerNotes(serverRequest.getServerNotes());
                server.setIpAddress(serverRequest.getIpAddress());
                server.setOperatingSystem(serverRequest.getOperatingSystem());
                server.setModifiedAt(LocalDateTime.now());

                Server updatedServer = serverRepository.save(server);
                return mapToServerResponse(updatedServer);
            } else {
                return mapToServerResponse(server);
            }
        } else {
            throw new NotFoundCustomException("Server not found with id: " + serverId);
        }
    }

    public ServerResponse removeAppFromServer(Integer serverId, Integer applicationId) {
        Optional<Server> optionalServer = serverRepository.findById(serverId);
        if (optionalServer.isPresent()) {
            Server server = optionalServer.get();
            Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new NotFoundCustomException("Application not found with id: " + applicationId));

            List<Application> applications = server.getApplications();
            if (applications.contains(application)) {
                applications.remove(application);
                application.getServers().remove(server);

                server.setApplications(applications);
                Server updatedServer = serverRepository.save(server);
                return mapToServerResponse(updatedServer);
            } else {
                return mapToServerResponse(server);
            }
        } else {
            throw new NotFoundCustomException("Server not found with id: " + serverId);
        }
    }

    public ServerResponse addDbToServer(Integer serverId, Integer dbId, ServerRequest serverRequest) {
        Optional<Server> optionalServer = serverRepository.findById(serverId);
        if (optionalServer.isPresent()) {
            Server server = optionalServer.get();
            Database database = databaseRepository.findById(dbId).orElseThrow(() -> new NotFoundCustomException("Database not found with id: " + dbId));
            if (database.getDeletedAt() != null) {
                throw new NotFoundCustomException("Database not found with id: " + dbId);
            }
            List<Database> databases = server.getDatabaseList();
            if (!databases.contains(database)) {
                server.getDatabaseList().add(database);
                server.setDatabaseList((databases));
                server.setServerName(serverRequest.getServerName());
                server.setDataSource(serverRequest.getDataSource());
                server.setType(serverRequest.getType());
                server.setRole(serverRequest.getRole());
                server.setCurrentNumberOfCores(serverRequest.getCurrentNumberOfCores());
                server.setCurrentRamGb(serverRequest.getCurrentRamGb());
                server.setCurrentDiskGb(serverRequest.getCurrentDiskGb());
                server.setPowerStatus(serverRequest.getPowerStatus());
                server.setServerNotes(serverRequest.getServerNotes());
                server.setIpAddress(serverRequest.getIpAddress());
                server.setOperatingSystem(serverRequest.getOperatingSystem());
                server.setModifiedAt(LocalDateTime.now());
                Server updatedServer = serverRepository.save(server);
                return mapToServerResponse(updatedServer);
            } else {
                return mapToServerResponse(server);
            }
        } else {
            throw new NotFoundCustomException("Server not found with id: " + serverId);
        }
    }

    public ServerResponse removeDbFromServer(Integer serverId, Integer dbId) {
        Optional<Server> optionalServer = serverRepository.findById(serverId);
        if (optionalServer.isPresent()) {
            Server server = optionalServer.get();
            Database database = databaseRepository.findById(dbId).orElseThrow(() -> new NotFoundCustomException("Database not found with id: " + dbId));

            List<Database> databases = server.getDatabaseList();
            if (databases.contains(database)) {
                server.getDatabaseList().remove(database);
                database.removeServer(server);
                Server updatedServer = serverRepository.save(server);
                return mapToServerResponse(updatedServer);
            } else {
                return mapToServerResponse(server);
            }
        } else {
            throw new NotFoundCustomException("Server not found with id: " + serverId);
        }
    }

    public List<Application> getNonArchivedServerApplications(Integer serverId) {
        Optional<Server> optionalServer = serverRepository.findById(serverId);
        if (optionalServer.isPresent()) {
            Server server = optionalServer.get();
            List<Application> applications = new ArrayList<>();
            for (Application application : server.getApplications()) {
                for (Server AppServer : application.getServers()) {
                    if (AppServer.getId().equals(server.getId()) && application.getDeletedAt() != null) {
                        applications.add(application);
                        break;
                    }
                }
            }
            return applications;
        } else {
            throw new NotFoundCustomException("Server not found with id: " + serverId);
        }
    }

    public List<Database> getNonArchivedServerDatabases(Integer serverId) {
        Optional<Server> optionalServer = serverRepository.findById(serverId);
        if (optionalServer.isPresent()) {
            Server server = optionalServer.get();
            List<Database> databases = new ArrayList<>();
            for (Database database : server.getDatabaseList()) {
                for (Server DbServer : database.getServerList()) {
                    if (DbServer.getId().equals(server.getId()) && database.getDeletedAt() != null) {
                        databases.add(database);
                        break;
                    }
                }
            }
            return databases;
        } else {
            throw new NotFoundCustomException("Server not found with id: " + serverId);
        }
    }


    public List<ServerResponse> getAllServers(Pageable paging) {

        Iterable<Server> servers = serverRepository.findAll(paging);

        List<ServerResponse> serverResponses = new ArrayList<>();

        servers.forEach(server ->
                serverResponses.add(ServerResponse
                        .builder()
                        .id(server.getId())
                        .serverName(server.getServerName())
                        .dataSource(server.getDataSource())
                        .type(server.getType())
                        .role(server.getRole())
                        .currentNumberOfCores(server.getCurrentNumberOfCores())
                        .currentRamGb(server.getCurrentRamGb())
                        .currentDiskGb(server.getCurrentDiskGb())
                        .powerStatus(server.getPowerStatus())
                        .serverNotes(server.getServerNotes())
                        .ipAddress(server.getIpAddress())
                        .databaseList(server.getDatabaseList())
                        .operatingSystem(server.getOperatingSystem())
                        .applications(server.getApplications())
                        .modifiedAt(server.getModifiedAt())
                        .deletedAt(server.getDeletedAt())
                        .createdAt(server.getCreatedAt())
                        .build())
        );

        return serverResponses;
    }

    public void deleteServerById(Integer id) {
        Optional<Server> optionalServer = serverRepository.findById(id);
        if (optionalServer.isPresent()) {
            Server server = optionalServer.get();
            for (Application application : server.getApplications()) {
                application.getServers().remove(server);
            }
            for (Database database : server.getDatabaseList()) {
                database.getServerList().remove(server);
            }
            serverRepository.delete(optionalServer.get());
        } else {
            throw new NotFoundCustomException("Server not found with id: " + id);
        }
    }

    public ServerResponse updateServer(Integer id, ServerRequest serverRequest) {
        Optional<Server> optionalServer = serverRepository.findById(id);
        if (optionalServer.isPresent()) {
            LocalDateTime modified = LocalDateTime.now();
            Server server = optionalServer.get();
            server.setServerName(serverRequest.getServerName());
            server.setDataSource(serverRequest.getDataSource());
            server.setType(serverRequest.getType());
            server.setRole(serverRequest.getRole());
            server.setCurrentNumberOfCores(serverRequest.getCurrentNumberOfCores());
            server.setCurrentRamGb(serverRequest.getCurrentRamGb());
            server.setCurrentDiskGb(serverRequest.getCurrentDiskGb());
            server.setPowerStatus(serverRequest.getPowerStatus());
            server.setServerNotes(serverRequest.getServerNotes());
            server.setIpAddress(serverRequest.getIpAddress());
            server.setOperatingSystem(serverRequest.getOperatingSystem());

            server.setModifiedAt((modified));

            Server updatedServer = serverRepository.save(server);

            return mapToServerResponse(updatedServer);
        } else {
            throw new NotFoundCustomException("Server not found with id: " + id);
        }
    }

    public void deleteServerSoft(Integer id) {
        Optional<Server> optionalServer = serverRepository.findById(id);
        LocalDateTime deleted = LocalDateTime.now();
        if (optionalServer.isPresent()) {
            Server server = optionalServer.get();
            server.setDeletedAt(deleted);
            serverRepository.save(server);
            log.info("server {} archived successfully", server.getId());
        } else {
            throw new NotFoundCustomException("Server not found with id: " + id);
        }


    }

    public List<ServerResponse> getAllNonArchivedServers(Pageable paging) {

        Iterable<Server> servers = serverRepository.findAllByDeletedAtNull(paging);

        List<ServerResponse> serverResponses = new ArrayList<>();

        servers.forEach(server ->
                serverResponses.add(ServerResponse
                        .builder()
                        .id(server.getId())
                        .serverName(server.getServerName())
                        .dataSource(server.getDataSource())
                        .type(server.getType())
                        .role(server.getRole())
                        .currentNumberOfCores(server.getCurrentNumberOfCores())
                        .currentRamGb(server.getCurrentRamGb())
                        .currentDiskGb(server.getCurrentDiskGb())
                        .powerStatus(server.getPowerStatus())
                        .serverNotes(server.getServerNotes())
                        .ipAddress(server.getIpAddress())
                        .operatingSystem(server.getOperatingSystem())
                        .applications(server.getApplications())
                        .databaseList(server.getDatabaseList())
                        .modifiedAt(server.getModifiedAt())
                        .deletedAt(server.getDeletedAt())
                        .createdAt(server.getCreatedAt())
                        .datacenter(server.getDatacenter())
                        .environment(server.getEnvironment())
                        .build())
        );

        return serverResponses;
    }

    public List<ServerResponse> getAllArchivedServers(Pageable paging) {

        Iterable<Server> servers = serverRepository.findAllByDeletedAtIsNotNull(paging);

        List<ServerResponse> serverResponses = new ArrayList<>();

        servers.forEach(server ->
                serverResponses.add(ServerResponse
                        .builder()
                        .id(server.getId())
                        .serverName(server.getServerName())
                        .dataSource(server.getDataSource())
                        .type(server.getType())
                        .role(server.getRole())
                        .currentNumberOfCores(server.getCurrentNumberOfCores())
                        .currentRamGb(server.getCurrentRamGb())
                        .currentDiskGb(server.getCurrentDiskGb())
                        .powerStatus(server.getPowerStatus())
                        .serverNotes(server.getServerNotes())
                        .ipAddress(server.getIpAddress())
                        .operatingSystem(server.getOperatingSystem())
                        .applications(server.getApplications())
                        .databaseList((server.getDatabaseList()))
                        .modifiedAt(server.getModifiedAt())
                        .deletedAt(server.getDeletedAt())
                        .createdAt(server.getCreatedAt())
                        .datacenter(server.getDatacenter())
                        .environment(server.getEnvironment())
                        .build())
        );

        return serverResponses;
    }

    public List<ServerResponse> findByServerName(String serverName) {

        List<Server> servers = serverRepository.findByServerName(serverName);
        if (servers != null) {
            return servers.stream()
                    .filter(server -> server.getDeletedAt() != null)
                    .map(this::mapToServerResponse)
                    .toList();
        } else {
            throw new NotFoundCustomException("Server not found with name: " + serverName);
        }
    }

    public ServerResponse findById(Integer id) {
        Optional<Server> server = serverRepository.findById(id);

        if (server.isPresent()) {
            Server serv = server.get();
            if (serv.getDeletedAt() != null) {
                throw new NotFoundCustomException("Server not found with id: " + id);
            }
            return ServerResponse
                    .builder()
                    .id(serv.getId())
                    .serverName(serv.getServerName())
                    .dataSource(serv.getDataSource())
                    .type(serv.getType())
                    .role(serv.getRole())
                    .currentNumberOfCores(serv.getCurrentNumberOfCores())
                    .currentRamGb(serv.getCurrentRamGb())
                    .currentDiskGb(serv.getCurrentDiskGb())
                    .powerStatus(serv.getPowerStatus())
                    .serverNotes(serv.getServerNotes())
                    .ipAddress(serv.getIpAddress())
                    .operatingSystem(serv.getOperatingSystem())
                    .applications(serv.getApplications())
                    .databaseList(serv.getDatabaseList())
                    .modifiedAt(serv.getModifiedAt())
                    .deletedAt(serv.getDeletedAt())
                    .createdAt(serv.getCreatedAt())
                    .datacenter(serv.getDatacenter())
                    .environment(serv.getEnvironment())
                    .build();


            //return apps.stream().map(this::mapToApplicationResponse).toList();
        } else {
            throw new NotFoundCustomException("Server not found with id: " + id);
        }

    }

    private ServerResponse mapToServerResponse(Server server) {
        return ServerResponse.builder().id(server.getId())
                .serverName(server.getServerName())
                .dataSource(server.getDataSource())
                .type(server.getType())
                .role(server.getRole())
                .currentNumberOfCores(server.getCurrentNumberOfCores())
                .currentRamGb(server.getCurrentRamGb())
                .currentDiskGb(server.getCurrentDiskGb())
                .powerStatus(server.getPowerStatus())
                .serverNotes(server.getServerNotes())
                .ipAddress(server.getIpAddress())
                .operatingSystem(server.getOperatingSystem())
                .applications(server.getApplications())
                .databaseList(server.getDatabaseList())
                .modifiedAt(server.getModifiedAt())
                .deletedAt(server.getDeletedAt())
                .createdAt(server.getCreatedAt())
                .datacenter(server.getDatacenter())
                .environment(server.getEnvironment())
                .build();
    }

    public List<ServerResponse> getServerAll() {

        Iterable<Server> servers = serverRepository.findAllByDeletedAtIsNull();

        List<ServerResponse> serverResponses = new ArrayList<>();

        servers.forEach(server ->
                serverResponses.add(ServerResponse
                        .builder()
                        .id(server.getId())
                        .serverName(server.getServerName())
                        .dataSource(server.getDataSource())
                        .type(server.getType())
                        .role(server.getRole())
                        .currentNumberOfCores(server.getCurrentNumberOfCores())
                        .currentRamGb(server.getCurrentRamGb())
                        .currentDiskGb(server.getCurrentDiskGb())
                        .powerStatus(server.getPowerStatus())
                        .serverNotes(server.getServerNotes())
                        .ipAddress(server.getIpAddress())
                        .operatingSystem(server.getOperatingSystem())
                        .applications(server.getApplications())
                        .databaseList(server.getDatabaseList())
                        .modifiedAt(server.getModifiedAt())
                        .deletedAt(server.getDeletedAt())
                        .createdAt(server.getCreatedAt())
                                .datacenter(server.getDatacenter())
                                .environment(server.getEnvironment())
                        .build())
        );

        return serverResponses;
    }

    public ServerResponse mapToDataCenter(Integer serverId,Integer DataCenterId){
        Optional<Server> server = serverRepository.findById(serverId);
        Optional<DataCenter> dataCenter = dataCenterRepository.findById(DataCenterId);
        if (server.isPresent() && dataCenter.isPresent()) {
            Server serv = server.get();
            DataCenter data = dataCenter.get();
            serv.setDatacenter(data);
            serverRepository.save(serv);

            return ServerResponse
                    .builder()
                    .id(serv.getId())
                    .serverName(serv.getServerName())
                    .dataSource(serv.getDataSource())
                    .type(serv.getType())
                    .role(serv.getRole())
                    .currentNumberOfCores(serv.getCurrentNumberOfCores())
                    .currentRamGb(serv.getCurrentRamGb())
                    .currentDiskGb(serv.getCurrentDiskGb())
                    .powerStatus(serv.getPowerStatus())
                    .serverNotes(serv.getServerNotes())
                    .ipAddress(serv.getIpAddress())
                    .operatingSystem(serv.getOperatingSystem())
                    .applications(serv.getApplications())
                    .databaseList(serv.getDatabaseList())
                    .modifiedAt(serv.getModifiedAt())
                    .deletedAt(serv.getDeletedAt())
                    .createdAt(serv.getCreatedAt())
                    .datacenter(serv.getDatacenter())
                    .environment(serv.getEnvironment())
                    .build();
        } else {
            throw new NotFoundCustomException("Server not found with id: " + serverId);
        }
    }

    public ServerResponse mapToEnvironment(Integer serverId,Integer EnvironmentId){
        Optional<Server> server = serverRepository.findById(serverId);
        Optional<Environment> environment = environmentRepository.findById(EnvironmentId);
        if (server.isPresent() && environment.isPresent()) {
            Server serv = server.get();
            Environment data = environment.get();
            serv.setEnvironment(data);
            serverRepository.save(serv);
            return ServerResponse
                    .builder()
                    .id(serv.getId())
                    .serverName(serv.getServerName())
                    .dataSource(serv.getDataSource())
                    .type(serv.getType())
                    .role(serv.getRole())
                    .currentNumberOfCores(serv.getCurrentNumberOfCores())
                    .currentRamGb(serv.getCurrentRamGb())
                    .currentDiskGb(serv.getCurrentDiskGb())
                    .powerStatus(serv.getPowerStatus())
                    .serverNotes(serv.getServerNotes())
                    .ipAddress(serv.getIpAddress())
                    .operatingSystem(serv.getOperatingSystem())
                    .applications(serv.getApplications())
                    .databaseList(serv.getDatabaseList())
                    .modifiedAt(serv.getModifiedAt())
                    .deletedAt(serv.getDeletedAt())
                    .createdAt(serv.getCreatedAt())
                    .datacenter(serv.getDatacenter())
                    .environment(serv.getEnvironment())
                    .build();
        } else {
            throw new NotFoundCustomException("Server not found with id: " + serverId);
        }
    }



}
