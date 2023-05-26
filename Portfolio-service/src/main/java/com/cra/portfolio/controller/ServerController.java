package com.cra.portfolio.controller;

import com.cra.portfolio.dto.ApplicationRequest;
import com.cra.portfolio.dto.ApplicationResponse;
import com.cra.portfolio.dto.ServerRequest;
import com.cra.portfolio.dto.ServerResponse;
import com.cra.portfolio.model.Application;
import com.cra.portfolio.model.Database;
import com.cra.portfolio.model.Server;
import com.cra.portfolio.service.ApplicationService;
import com.cra.portfolio.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/servers")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ServerController {

    private final ServerService serverService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServerResponse createServer(@RequestBody ServerRequest serverRequest) {
        return (serverService.createServer(serverRequest));
    }

    @GetMapping()
    public ResponseEntity<List<ServerResponse>> getAllServers(
            @RequestParam(defaultValue = "10", required = false)
            Integer pageSize,
            @RequestParam(defaultValue = "0", required = false)
            Integer page

    ) {

        Pageable paging = PageRequest.of(page, pageSize);

        List<ServerResponse> serverResponses =
                serverService.getAllServers(paging);

        return new ResponseEntity<>(
                serverResponses, HttpStatus.CREATED);
    }

    @PutMapping("/{serverId}/application/link/{applicationId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ServerResponse> addAppToServer(@PathVariable Integer serverId, @PathVariable Integer applicationId, @RequestBody ServerRequest serverRequest) {
        ServerResponse updatedServer = serverService.addAppToServer(serverId, applicationId, serverRequest);
        return ResponseEntity.ok(updatedServer);
    }

    @PutMapping("/{serverId}/application/unlink/{applicationId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ServerResponse> removeAppFromServer(@PathVariable Integer serverId, @PathVariable Integer applicationId) {
        ServerResponse updatedServer = serverService.removeAppFromServer(serverId, applicationId);
        return ResponseEntity.ok(updatedServer);
    }

    @PutMapping("/{serverId}/database/link/{DbId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ServerResponse> addDbToServer(@PathVariable Integer serverId, @PathVariable Integer DbId, @RequestBody ServerRequest serverRequest) {
        ServerResponse updatedServer = serverService.addDbToServer(serverId, DbId, serverRequest);
        return ResponseEntity.ok(updatedServer);
    }

    @PutMapping("/{serverId}/database/unlink/{dbId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ServerResponse> removeDbFromServer(@PathVariable Integer serverId, @PathVariable Integer dbId) {
        ServerResponse updatedServer = serverService.removeDbFromServer(serverId, dbId);
        return ResponseEntity.ok(updatedServer);
    }

    @GetMapping("/archived")
    public ResponseEntity<List<ServerResponse>> getAllArchivedServers(
            @RequestParam(defaultValue = "5", required = false)
            Integer pageSize,
            @RequestParam(defaultValue = "0", required = false)
            Integer page

    ) {

        Pageable paging = PageRequest.of(page, pageSize);

        List<ServerResponse> serverResponses =
                serverService.getAllArchivedServers(paging);

        return new ResponseEntity<>(
                serverResponses, HttpStatus.CREATED);
    }

    @GetMapping("/{serverId}/applications")
    public List<Application> getNonArchivedServerApplications(@PathVariable Integer serverId) {
        return serverService.getNonArchivedServerApplications(serverId);
    }

    @GetMapping("/{serverId}/databases")
    public List<Database> getNonArchivedServerDatabases(@PathVariable Integer serverId) {
        return serverService.getNonArchivedServerDatabases(serverId);
    }

    @GetMapping("/non-archived")
    public ResponseEntity<List<ServerResponse>> getAllNonArchivedServers(
            @RequestParam(defaultValue = "5", required = false)
            Integer pageSize,
            @RequestParam(defaultValue = "0", required = false)
            Integer page

    ) {

        Pageable paging = PageRequest.of(page, pageSize);

        List<ServerResponse> serverResponses =
                serverService.getAllNonArchivedServers(paging);

        return new ResponseEntity<>(
                serverResponses, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<ServerResponse> deleteServerById(@PathVariable Integer id) {
        serverService.deleteServerById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServerResponse> updateServer(@PathVariable Integer id, @RequestBody ServerRequest serverRequest) {
        ServerResponse updatedServer = serverService.updateServer(id, serverRequest);
        return ResponseEntity.ok(updatedServer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServerSoft(@PathVariable Integer id) {
        serverService.deleteServerSoft(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ServerResponse findServerById(@PathVariable Integer id) {
        return serverService.findById(id);
    }

    @GetMapping("/name/{serverName}")
    @ResponseStatus(HttpStatus.OK)
    public List<ServerResponse> findServerByName(@PathVariable String serverName) {
        return serverService.findByServerName(serverName);

    }

    @GetMapping("/all")
    public List<ServerResponse> getNonArchivedServer() {
        return serverService.getServerAll();
    }

    @PutMapping("/{serverId}/datacenter/link/{datacenterId}")
    public ResponseEntity<ServerResponse> addDatacenterToServer(@PathVariable Integer serverId, @PathVariable Integer datacenterId) {
        ServerResponse updatedServer = serverService.mapToDataCenter(serverId, datacenterId);
        return ResponseEntity.ok(updatedServer);
    }
    @PutMapping("/{serverId}/environment/link/{environmentId}")
    public ResponseEntity<ServerResponse> addEnvironmentToServer(@PathVariable Integer serverId, @PathVariable Integer environmentId) {
        ServerResponse updatedServer = serverService.mapToEnvironment(serverId, environmentId);
        return ResponseEntity.ok(updatedServer);
    }


}
