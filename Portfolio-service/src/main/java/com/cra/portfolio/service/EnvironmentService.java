package com.cra.portfolio.service;

import com.cra.portfolio.dto.DataCenterResponse;
import com.cra.portfolio.dto.DatabaseResponse;
import com.cra.portfolio.dto.EnvironmentRequest;
import com.cra.portfolio.dto.EnvironmentResponse;
import com.cra.portfolio.exception.NotFoundCustomException;
import com.cra.portfolio.model.DataCenter;
import com.cra.portfolio.model.Database;
import com.cra.portfolio.model.Environment;
import com.cra.portfolio.model.Server;
import com.cra.portfolio.repository.EnvironmentRepository;
import com.cra.portfolio.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnvironmentService {
    private final EnvironmentRepository environmentRepository;
    private final ServerRepository serverRepository;

    public EnvironmentResponse createEnvironment(EnvironmentRequest request) {
        Environment env= Environment.builder()

                .environmentName(request.getEnvironmentName())
                .type(request.getType())
                .description(request.getDescription())
                .createdAt(java.time.LocalDateTime.now())
                .build();
        environmentRepository.save(env);
        log.info("environment {} created successfully", env.getEnvironmentName());
        return mapToEnvironmentResponse(env);
    }

    public List<EnvironmentResponse> getAllEnvironments(Pageable paging) {

        Iterable<Environment> environments = environmentRepository.findAll(paging);

        List<EnvironmentResponse> environmentResponses = new ArrayList<>();

        environments.forEach(environment ->
                environmentResponses.add(EnvironmentResponse
                        .builder()
                                .id(environment.getId())
                        .environmentName(environment.getEnvironmentName())
                        .type(environment.getType())
                        .description(environment.getDescription())
                        .servers(environment.getServers())
                                .createdAt(environment.getCreatedAt())
                                .modifiedAt(environment.getModifiedAt())
                                .deletedAt(environment.getDeletedAt())
                        .build())
        );

        return environmentResponses;
    }

    public List<EnvironmentResponse> getAllNonArchivedEnvironments(Pageable paging) {

        Iterable<Environment> environments = environmentRepository.findAllByDeletedAtNull(paging);

        List<EnvironmentResponse> environmentResponses = new ArrayList<>();

        environments.forEach(environment ->
                environmentResponses.add(EnvironmentResponse
                        .builder()
                                .id(environment.getId())
                        .environmentName(environment.getEnvironmentName())
                        .type(environment.getType())
                        .description(environment.getDescription())
                        .servers(environment.getServers())
                        .createdAt(environment.getCreatedAt())
                        .modifiedAt(environment.getModifiedAt())
                        .deletedAt(environment.getDeletedAt())


                        .build())
        );

        return environmentResponses;
    }

    public void deleteEnvironmentById(Integer id){
        Optional<Environment> environment = environmentRepository.findById(id);
        if (environment.isPresent()) {
            Environment env = environment.get();
            List<Server>servers=env.getServers();

            for(Server server:servers){
                server.setEnvironment(null);
                serverRepository.save(server);
            }

            environmentRepository.deleteById(id);

        }

    }


public void deleteEnvironmentSoft(Integer id){
        Optional<Environment> environment = environmentRepository.findById(id);
        if (environment.isPresent()) {
            Environment env = environment.get();
            List<Server>servers=env.getServers();

            for(Server server:servers){
                server.setEnvironment(null);
                serverRepository.save(server);
            }
            env.setDeletedAt(java.time.LocalDateTime.now());
            environmentRepository.save(env);
        }

    }

    public EnvironmentResponse updateEnvironment(Integer id, EnvironmentRequest request) {
        Optional<Environment> environment = environmentRepository.findById(id);
        if (environment.isPresent()) {
            Environment env = environment.get();
            env.setEnvironmentName(request.getEnvironmentName());
            env.setType(request.getType());
            env.setDescription(request.getDescription());
            env.setModifiedAt(java.time.LocalDateTime.now());
            environmentRepository.save(env);
            log.info("environment {} updated successfully", env.getEnvironmentName());
            return mapToEnvironmentResponse(env);
        }
        else {
            throw new NotFoundCustomException("Environment not found with id: " + id);
        }
    }

    public EnvironmentResponse getEnvironmentById(Integer id) {
        Optional<Environment> environment = environmentRepository.findById(id);
        if (environment.isPresent()) {
            Environment env = environment.get();
            return mapToEnvironmentResponse(env);
        }
        else {
            throw new NotFoundCustomException("Environment not found with id: " + id);
        }
    }

    public EnvironmentResponse addServerToEnvironment(Integer id, Integer serverId) {
        Optional<Environment> environment = environmentRepository.findById(id);
        Optional<Server> server = serverRepository.findById(serverId);
        if (environment.isPresent() && server.isPresent()) {
            Environment env = environment.get();
            Server serv = server.get();
            List<Server> servers = env.getServers();
            servers.add(serv);
            env.setServers(servers);
            for (Server updatedServer : servers) {
                updatedServer.setEnvironment(env);
                serverRepository.save(serv);
            }
            environmentRepository.save(env);
            log.info("server {} added to environment {} successfully", serv.getServerName(), env.getEnvironmentName());
            return mapToEnvironmentResponse(env);
        }
        else {
            throw new NotFoundCustomException("Environment or Server not found with id: " + id + " " + serverId);
        }
    }

    public List<Server> getServersByEnvironmentId(Integer id) {
        Optional<Environment> environment = environmentRepository.findById(id);
        if (environment.isPresent()) {
            Environment env = environment.get();
            return env.getServers();
        }
        else {
            throw new NotFoundCustomException("Environment not found with id: " + id);
        }
    }

    public List<EnvironmentResponse> getEnvironmentAll() {

        Iterable<Environment> environments = environmentRepository.findAllByDeletedAtIsNull();

        List<EnvironmentResponse> environmentResponses = new ArrayList<>();

        environments.forEach(environment ->
                environmentResponses.add(EnvironmentResponse
                        .builder()
                        .id(environment.getId())
                        .environmentName(environment.getEnvironmentName())
                        .type(environment.getType())
                        .description(environment.getDescription())
                        .servers(environment.getServers())
                        .createdAt(environment.getCreatedAt())
                        .modifiedAt(environment.getModifiedAt())
                        .deletedAt(environment.getDeletedAt())


                        .build())
        );

        return environmentResponses;
    }





    private EnvironmentResponse mapToEnvironmentResponse(Environment env) {
        return EnvironmentResponse.builder()
                .id(env.getId())
                .environmentName(env.getEnvironmentName())
                .type(env.getType())
                .description(env.getDescription())
                .servers(env.getServers())
                .createdAt(env.getCreatedAt())
                .modifiedAt(env.getModifiedAt())
                .deletedAt(env.getDeletedAt())
                .build();
    }

}
