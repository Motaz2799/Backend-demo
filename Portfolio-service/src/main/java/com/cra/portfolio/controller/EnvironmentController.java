package com.cra.portfolio.controller;

import com.cra.portfolio.dto.*;
import com.cra.portfolio.model.Server;
import com.cra.portfolio.service.EnvironmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/environments")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EnvironmentController {
    private final EnvironmentService environmentService;
    @GetMapping("/servers/{id}")
    public ResponseEntity<List<Server>> getServersByEnvironmentId(@PathVariable Integer id) {
        return ResponseEntity.ok(environmentService.getServersByEnvironmentId(id));
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EnvironmentResponse createEnvironment(@RequestBody EnvironmentRequest environmentRequest) {
        return environmentService.createEnvironment(environmentRequest);
    }

    @GetMapping()
    public ResponseEntity<List<EnvironmentResponse>> getDatabases(
            @RequestParam(defaultValue = "5", required = false)
            Integer pageSize,
            @RequestParam(defaultValue = "0", required = false)
            Integer page

    ) {

        Pageable paging = PageRequest.of(page, pageSize);

        List<EnvironmentResponse> environmentResponses =
                environmentService.getAllEnvironments(paging);

        return new ResponseEntity<>(
                environmentResponses, HttpStatus.CREATED);
    }

    @GetMapping("/non-archived")
    public ResponseEntity<List<EnvironmentResponse>> getAllNonArchivedEnvironments(
            @RequestParam(defaultValue = "5", required = false)
            Integer pageSize,
            @RequestParam(defaultValue = "0", required = false)
            Integer page

    ) {

        Pageable paging = PageRequest.of(page, pageSize);

        List<EnvironmentResponse> environmentResponses =
                environmentService.getAllNonArchivedEnvironments(paging);

        return new ResponseEntity<>(
                environmentResponses, HttpStatus.CREATED);
    }

    @DeleteMapping("/{environmentId}/hard")
    public ResponseEntity<EnvironmentResponse> deleteEnvironmentById(@PathVariable Integer environmentId) {
        environmentService.deleteEnvironmentById(environmentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnvironmentSoft(@PathVariable Integer id) {
        environmentService.deleteEnvironmentSoft(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EnvironmentResponse findEnvironmentById(@PathVariable Integer id) {
        return environmentService.getEnvironmentById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnvironmentResponse> updateEnvironment(@PathVariable Integer id, @RequestBody EnvironmentRequest environmentRequest) {
        EnvironmentResponse updatedEnvironment = environmentService.updateEnvironment(id, environmentRequest);
        return ResponseEntity.ok(updatedEnvironment);
    }

    @PutMapping("/{environmentId}/server/link/{ServerId}")
@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EnvironmentResponse> addServerToEnvironment(@PathVariable Integer environmentId, @PathVariable Integer ServerId) {
        EnvironmentResponse updatedEnvironment = environmentService.addServerToEnvironment(environmentId, ServerId);
        return ResponseEntity.ok(updatedEnvironment);
    }

    @GetMapping("/all")
    public List<EnvironmentResponse> getNonArchivedEnvironment() {
        return environmentService.getEnvironmentAll();
    }



}
