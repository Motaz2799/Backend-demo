package com.cra.portfolio.service;

import com.cra.portfolio.dto.DatabaseResponse;
import com.cra.portfolio.dto.DataCenterRequest;
import com.cra.portfolio.dto.DataCenterResponse;
import com.cra.portfolio.dto.ServerResponse;
import com.cra.portfolio.exception.NotFoundCustomException;
import com.cra.portfolio.model.Database;
import com.cra.portfolio.model.DataCenter;
import com.cra.portfolio.model.Server;
import com.cra.portfolio.repository.DataCenterRepository;
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
public class DataCenterService {
    private final DataCenterRepository dataCenterRepository;
    private final ServerRepository serverRepository;

    public DataCenterResponse createDataCenter(DataCenterRequest request) {
        DataCenter dc= DataCenter.builder()

                .name(request.getName())
                .notes(request.getNotes())
                .location(request.getLocation())
                .contractExpirationDate(request.getContractExpirationDate())
                .createdAt(java.time.LocalDateTime.now())
                .build();
        dataCenterRepository.save(dc);
        log.info("dataCenter {} created successfully", dc.getName());
        return mapToDataCenterResponse(dc);
    }

    public List<DataCenterResponse> getAllDataCenters(Pageable paging) {

        Iterable<DataCenter> dataCenters = dataCenterRepository.findAll(paging);

        List<DataCenterResponse> dataCenterResponses = new ArrayList<>();

        dataCenters.forEach(dataCenter ->
                dataCenterResponses.add(DataCenterResponse
                        .builder()
                        .id(dataCenter.getId())
                        .name(dataCenter.getName())
                        .location(dataCenter.getLocation())
                        .notes(dataCenter.getNotes())
                        .contractExpirationDate(dataCenter.getContractExpirationDate())
                        .servers(dataCenter.getServers())
                        .createdAt(dataCenter.getCreatedAt())
                        .modifiedAt(dataCenter.getModifiedAt())
                        .deletedAt(dataCenter.getDeletedAt())
                        .build())
        );

        return dataCenterResponses;
    }

    public List<DataCenterResponse> getAllNonArchivedDataCenters(Pageable paging) {

        Iterable<DataCenter> dataCenters = dataCenterRepository.findAllByDeletedAtNull(paging);

        List<DataCenterResponse> dataCenterResponses = new ArrayList<>();

        dataCenters.forEach(dataCenter ->
                dataCenterResponses.add(DataCenterResponse
                        .builder()
                        .id(dataCenter.getId())
                        .name(dataCenter.getName())
                        .location(dataCenter.getLocation())
                        .notes(dataCenter.getNotes())
                        .contractExpirationDate(dataCenter.getContractExpirationDate())
                        .servers(dataCenter.getServers())
                        .createdAt(dataCenter.getCreatedAt())
                        .modifiedAt(dataCenter.getModifiedAt())
                        .deletedAt(dataCenter.getDeletedAt())
                        .build())
        );

        return dataCenterResponses;
    }

    public void deleteDataCenterById(Integer id){
        Optional<DataCenter> dataCenter = dataCenterRepository.findById(id);
        if (dataCenter.isPresent()) {
            DataCenter dc = dataCenter.get();
            List<Server>servers=dc.getServers();

            for(Server server:servers){
                server.setDatacenter(null);
                serverRepository.save(server);
            }

            dataCenterRepository.deleteById(id);

        }

    }


    public void deleteDataCenterSoft(Integer id){
        Optional<DataCenter> dataCenter = dataCenterRepository.findById(id);
        if (dataCenter.isPresent()) {
            DataCenter dc = dataCenter.get();
            List<Server>servers=dc.getServers();

            for(Server server:servers){
                server.setDatacenter(null);
                serverRepository.save(server);
            }
            dc.setDeletedAt(java.time.LocalDateTime.now());
            dataCenterRepository.save(dc);
        }

    }

    public DataCenterResponse updateDataCenter(Integer id, DataCenterRequest request) {
        Optional<DataCenter> dataCenter = dataCenterRepository.findById(id);
        if (dataCenter.isPresent()) {
            DataCenter dc = dataCenter.get();
            dc.setName(request.getName());
            dc.setLocation(request.getLocation());
            dc.setContractExpirationDate(request.getContractExpirationDate());
            dc.setNotes(request.getNotes());
            dc.setModifiedAt(java.time.LocalDateTime.now());
            dataCenterRepository.save(dc);
            log.info("dataCenter {} updated successfully", dc.getName());
            return mapToDataCenterResponse(dc);
        }
        else {
            throw new NotFoundCustomException("DataCenter not found with id: " + id);
        }
    }

    public DataCenterResponse getDataCenterById(Integer id) {
        Optional<DataCenter> dataCenter = dataCenterRepository.findById(id);
        if (dataCenter.isPresent()) {
            DataCenter dc = dataCenter.get();
            return mapToDataCenterResponse(dc);
        }
        else {
            throw new NotFoundCustomException("DataCenter not found with id: " + id);
        }
    }

    public DataCenterResponse addServerToDataCenter(Integer id, Integer serverId) {
        Optional<DataCenter> dataCenter = dataCenterRepository.findById(id);
        Optional<Server> server = serverRepository.findById(serverId);
        if (dataCenter.isPresent() && server.isPresent()) {
            DataCenter dc = dataCenter.get();
            Server serv = server.get();
            List<Server> servers = dc.getServers();
            servers.add(serv);
            dc.setServers(servers);
            for (Server updatedServer : servers) {
                updatedServer.setDatacenter(dc);
                serverRepository.save(serv);
            }
            dataCenterRepository.save(dc);
            log.info("server {} added to dataCenter {} successfully", serv.getServerName(), dc.getName());
            return mapToDataCenterResponse(dc);
        }
        else {
            throw new NotFoundCustomException("DataCenter or Server not found with id: " + id + " " + serverId);
        }
    }

    public List<Server> getServersByDataCenterId(Integer id) {
        Optional<DataCenter> dataCenter = dataCenterRepository.findById(id);
        if (dataCenter.isPresent()) {
            DataCenter dc = dataCenter.get();
            return dc.getServers();
        }
        else {
            throw new NotFoundCustomException("DataCenter not found with id: " + id);
        }
    }






    private DataCenterResponse mapToDataCenterResponse(DataCenter dc) {
        return DataCenterResponse.builder()
                .id(dc.getId())
                .name(dc.getName())
                .location(dc.getLocation())
                .notes(dc.getNotes())
                .contractExpirationDate(dc.getContractExpirationDate())
                .servers(dc.getServers())
                .createdAt(dc.getCreatedAt())
                .modifiedAt(dc.getModifiedAt())
                .deletedAt(dc.getDeletedAt())
                .build();
    }

    public List<DataCenterResponse> getDataCenterAll() {

        Iterable<DataCenter> dataCenters = dataCenterRepository.findAllByDeletedAtIsNull();

        List<DataCenterResponse> dataCenterResponses = new ArrayList<>();

        dataCenters.forEach(dataCenter ->
                dataCenterResponses.add(DataCenterResponse
                        .builder()
                        .id(dataCenter.getId())
                        .name(dataCenter.getName())
                        .location(dataCenter.getLocation())
                        .notes(dataCenter.getNotes())
                        .contractExpirationDate(dataCenter.getContractExpirationDate())
                        .servers(dataCenter.getServers())
                        .createdAt(dataCenter.getCreatedAt())
                        .modifiedAt(dataCenter.getModifiedAt())
                        .deletedAt(dataCenter.getDeletedAt())
                        .build())
        );
        return dataCenterResponses;
    }
}
