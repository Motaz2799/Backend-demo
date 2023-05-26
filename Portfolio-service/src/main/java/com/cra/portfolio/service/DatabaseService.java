package com.cra.portfolio.service;

import com.cra.portfolio.dto.DatabaseRequest;
import com.cra.portfolio.dto.DatabaseResponse;
import com.cra.portfolio.dto.ServerResponse;
import com.cra.portfolio.exception.NotFoundCustomException;
import com.cra.portfolio.model.Application;
import com.cra.portfolio.model.Contact;
import com.cra.portfolio.model.Database;
import com.cra.portfolio.model.Server;
import com.cra.portfolio.repository.DatabaseRepository;
import com.cra.portfolio.repository.ServerRepository;
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
public class DatabaseService {
    private final DatabaseRepository databaseRepository;
    private final ServerRepository serverRepository;


    public DatabaseResponse createDatabase(DatabaseRequest databaseRequest) {
        Database database = Database.builder()
                .databaseName(databaseRequest.getNameDb())
                .databaseVersion(databaseRequest.getVersionDb())
                .build();
        databaseRepository.save(database);
        log.info("database {} created successfully", database.getDatabaseId());
        return mapToDatabaseResponse(database);
    }

    public DatabaseResponse addServerToDb(Integer dbId, Integer serverId, DatabaseRequest databaseRequest) {
        Optional<Database> optionalDatabase = databaseRepository.findById(dbId);
        if (optionalDatabase.isPresent()) {
            Database database = optionalDatabase.get();
            Server server = serverRepository.findById(serverId).orElseThrow(() -> new NotFoundCustomException("Server not found with id: " + serverId));
            if (server.getDeletedAt() != null) {
                throw new NotFoundCustomException("server not found with id: " + serverId);
            }
            List<Server> servers = database.getServerList();
            if (!servers.contains(server)) {
                servers.add(server);
                server.getDatabaseList().add(database);
                database.setServerList(servers);
                database.setDatabaseName(databaseRequest.getNameDb());
                database.setDatabaseVersion(databaseRequest.getVersionDb());
                database.setModifiedAt(LocalDateTime.now());
                Database updatedDatabase = databaseRepository.save(database);
                return mapToDatabaseResponse(updatedDatabase);
            } else {
                return mapToDatabaseResponse(database);
            }
        } else {
            throw new NotFoundCustomException("Database not found with id: " + dbId);
        }
    }

    public DatabaseResponse removeServerFromDb(Integer dbId, Integer serverId) {
        Optional<Database> optionalDatabase = databaseRepository.findById(dbId);
        if (optionalDatabase.isPresent()) {
            Database database = optionalDatabase.get();
            Server server = serverRepository.findById(serverId).orElseThrow(() -> new NotFoundCustomException("Server not found with id: " + serverId));

            List<Server> servers = database.getServerList();
            if (servers.contains(server)) {
                servers.remove(server);
                server.getDatabaseList().remove(database);

                database.setServerList(servers);
                Database updatedDatabase = databaseRepository.save(database);
                return mapToDatabaseResponse(updatedDatabase);
            } else {
                return mapToDatabaseResponse(database);
            }
        } else {
            throw new NotFoundCustomException("Database not found with id: " + dbId);
        }
    }

    public List<Server> getNonArchivedDatabaseServers(Integer databaseId) {
        Optional<Database> optionalDatabase = databaseRepository.findById(databaseId);
        if (optionalDatabase.isPresent()) {
            Database database = optionalDatabase.get();
            List<Server> servers = new ArrayList<>();
            for (Server server : database.getServerList()) {
                for (Database serverDb : server.getDatabaseList()) {
                    if (serverDb.getDatabaseId().equals(database.getDatabaseId()) && server.getDeletedAt() == null) {
                        servers.add(server);
                        break;
                    }
                }
            }
            return servers;
        } else {
            throw new NotFoundCustomException("Database not found with id: " + databaseId);
        }
    }

    public List<DatabaseResponse> getAllDatabases(Pageable paging) {

        Iterable<Database> databases = databaseRepository.findAll(paging);

        List<DatabaseResponse> databaseResponses = new ArrayList<>();

        databases.forEach(database ->
                databaseResponses.add(DatabaseResponse
                        .builder()
                        .id(database.getDatabaseId())
                        .nameDb(database.getDatabaseName())
                        .versionDb(database.getDatabaseVersion())
                        .serverList(database.getServerList())
                        .createdAt(database.getCreatedAt())
                        .modifiedAt(database.getModifiedAt())
                        .deletedAt(database.getDeletedAt())
                        .build())
        );

        return databaseResponses;
    }

    public List<DatabaseResponse> getAllNonArchivedDatabases(Pageable paging) {

        Iterable<Database> databases = databaseRepository.findAllByDeletedAtNull(paging);

        List<DatabaseResponse> databaseResponses = new ArrayList<>();


        databases.forEach(database ->
                databaseResponses.add(DatabaseResponse
                        .builder()
                        .id(database.getDatabaseId())
                        .nameDb(database.getDatabaseName())
                        .versionDb(database.getDatabaseVersion())
                        .serverList(database.getServerList())
                        .createdAt(database.getCreatedAt())
                        .modifiedAt(database.getModifiedAt())
                        .deletedAt(database.getDeletedAt())
                        .build())
        );

        return databaseResponses;

    }
    public List<DatabaseResponse> getAllDatabases() {

        Iterable<Database> databases = databaseRepository.findAllByDeletedAtNull();

        List<DatabaseResponse> databaseResponses = new ArrayList<>();


        databases.forEach(database ->
                databaseResponses.add(DatabaseResponse
                        .builder()
                        .id(database.getDatabaseId())
                        .nameDb(database.getDatabaseName())
                        .versionDb(database.getDatabaseVersion())
                        .serverList(database.getServerList())
                        .createdAt(database.getCreatedAt())
                        .modifiedAt(database.getModifiedAt())
                        .deletedAt(database.getDeletedAt())
                        .build())
        );

        return databaseResponses;

    }

    public List<DatabaseResponse> getAllArchivedDatabases(Pageable paging) {

        Iterable<Database> databases = databaseRepository.findAllByDeletedAtIsNotNull(paging);

        List<DatabaseResponse> databaseResponses = new ArrayList<>();


        databases.forEach(database ->
                databaseResponses.add(DatabaseResponse
                        .builder()
                        .id(database.getDatabaseId())
                        .nameDb(database.getDatabaseName())
                        .versionDb(database.getDatabaseVersion())
                        .serverList(database.getServerList())
                        .createdAt(database.getCreatedAt())
                        .modifiedAt(database.getModifiedAt())
                        .deletedAt(database.getDeletedAt())
                        .build())
        );

        return databaseResponses;

    }

    public void deleteDatabaseById(Integer id) {
        Optional<Database> optionalDatabase = databaseRepository.findById(id);
        if (optionalDatabase.isPresent()) {
            Database database = optionalDatabase.get();
            for (Server server : database.getServerList()) {
                server.removeDatabase(database);
            }
            databaseRepository.delete(optionalDatabase.get());
        } else {
            throw new NotFoundCustomException("Database not found with id: " + id);
        }
    }

    public void deleteDatabaseSoft(Integer id) {
        Optional<Database> optionalDatabase = databaseRepository.findById(id);

        if (optionalDatabase.isPresent()) {
            Database database = optionalDatabase.get();
            database.setDeletedAt(LocalDateTime.now());
            databaseRepository.save(database);
            log.info("database {} archived successfully", database.getDatabaseId());
        } else {
            throw new NotFoundCustomException("Database not found with id: " + id);
        }


    }

    public DatabaseResponse updateDatabase(Integer id, DatabaseRequest databaseRequest) {
        Optional<Database> optionalDatabase = databaseRepository.findById(id);
        if (optionalDatabase.isPresent()) {
            Database database = optionalDatabase.get();
            database.setDatabaseName(databaseRequest.getNameDb());
            database.setDatabaseVersion(databaseRequest.getVersionDb());
            database.setModifiedAt(LocalDateTime.now());
            Database updatedDatabase = databaseRepository.save(database);

            return mapToDatabaseResponse(updatedDatabase);
        } else {
            throw new NotFoundCustomException("Database not found with id: " + id);
        }
    }

    public DatabaseResponse findById(Integer id) {
        Optional<Database> database = databaseRepository.findById(id);

        if (database.isPresent()) {
            Database database1 = database.get();
            return DatabaseResponse
                    .builder()
                    .id(database1.getDatabaseId())
                    .nameDb(database1.getDatabaseName())
                    .versionDb(database1.getDatabaseVersion())
                    .serverList(database1.getServerList())
                    .build();


            //return apps.stream().map(this::mapToApplicationResponse).toList();
        } else {
            throw new NotFoundCustomException("Database not found with id: " + id);
        }

    }

    private DatabaseResponse mapToDatabaseResponse(Database database) {
        return DatabaseResponse.builder().id(database.getDatabaseId())
                .nameDb(database.getDatabaseName())
                .versionDb(database.getDatabaseVersion())
                .serverList(database.getServerList())
                .createdAt(database.getCreatedAt())
                .deletedAt(database.getDeletedAt())
                .modifiedAt(database.getModifiedAt())
                .build();
    }
    public DatabaseResponse mapToDatabaseResponsePublic(Database database) {
        return DatabaseResponse.builder().id(database.getDatabaseId())
                .nameDb(database.getDatabaseName())
                .versionDb(database.getDatabaseVersion())
                .serverList(database.getServerList())
                .createdAt(database.getCreatedAt())
                .deletedAt(database.getDeletedAt())
                .modifiedAt(database.getModifiedAt())
                .build();
    }


}
