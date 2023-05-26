package com.cra.portfolio.controller;

import com.cra.portfolio.dto.*;
import com.cra.portfolio.model.DataCenter;
import com.cra.portfolio.model.Server;
import com.cra.portfolio.service.DataCenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/datacenters")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DataCenterController {
    private final DataCenterService dataCenterService;

    @GetMapping("/servers/{id}")
    public ResponseEntity<List<Server>> getServersByDataCenterId(@PathVariable Integer id) {
        return ResponseEntity.ok(dataCenterService.getServersByDataCenterId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DataCenterResponse createDataCenter(@RequestBody DataCenterRequest dataCenterRequest) {
        return dataCenterService.createDataCenter(dataCenterRequest);
    }

    @GetMapping()
    public ResponseEntity<List<DataCenterResponse>> getDatabases(
            @RequestParam(defaultValue = "5", required = false)
            Integer pageSize,
            @RequestParam(defaultValue = "0", required = false)
            Integer page

    ) {

        Pageable paging = PageRequest.of(page, pageSize);

        List<DataCenterResponse> dataCenterResponses =
                dataCenterService.getAllDataCenters(paging);

        return new ResponseEntity<>(
                dataCenterResponses, HttpStatus.CREATED);
    }

    @GetMapping("/non-archived")
    public ResponseEntity<List<DataCenterResponse>> getAllNonArchivedDataCenters(
            @RequestParam(defaultValue = "5", required = false)
            Integer pageSize,
            @RequestParam(defaultValue = "0", required = false)
            Integer page

    ) {

        Pageable paging = PageRequest.of(page, pageSize);

        List<DataCenterResponse> dataCenterResponses =
                dataCenterService.getAllNonArchivedDataCenters(paging);

        return new ResponseEntity<>(
                dataCenterResponses, HttpStatus.CREATED);
    }

    @DeleteMapping("/{dataCenterId}/hard")
    public ResponseEntity<DataCenterResponse> deleteDataCenterById(@PathVariable Integer dataCenterId) {
        dataCenterService.deleteDataCenterById(dataCenterId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDataCenterSoft(@PathVariable Integer id) {
        dataCenterService.deleteDataCenterSoft(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DataCenterResponse findDataCenterById(@PathVariable Integer id) {
        return dataCenterService.getDataCenterById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataCenterResponse> updateDataCenter(@PathVariable Integer id, @RequestBody DataCenterRequest dataCenterRequest) {
        DataCenterResponse updatedDataCenter = dataCenterService.updateDataCenter(id, dataCenterRequest);
        return ResponseEntity.ok(updatedDataCenter);
    }

    @PutMapping("/{dataCenterId}/server/link/{ServerId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DataCenterResponse> addServerToDataCenter(@PathVariable Integer dataCenterId, @PathVariable Integer ServerId) {
        DataCenterResponse updatedDataCenter = dataCenterService.addServerToDataCenter(dataCenterId, ServerId);
        return ResponseEntity.ok(updatedDataCenter);
    }

    @GetMapping("/all")
    public List<DataCenterResponse> getNonArchivedDataCenter() {
        return dataCenterService.getDataCenterAll();
    }




}
