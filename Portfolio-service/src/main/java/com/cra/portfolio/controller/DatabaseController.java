package com.cra.portfolio.controller;

import com.cra.portfolio.dto.ApplicationResponse;
import com.cra.portfolio.dto.DatabaseRequest;
import com.cra.portfolio.dto.DatabaseResponse;
import com.cra.portfolio.dto.ServerResponse;
import com.cra.portfolio.model.Server;
import com.cra.portfolio.service.DatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/databases")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DatabaseController {
    private final DatabaseService databaseService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DatabaseResponse createDatabase(@RequestBody DatabaseRequest databaseRequest) {
        return databaseService.createDatabase(databaseRequest);
    }

    @GetMapping()
    public ResponseEntity<List<DatabaseResponse>> getDatabases(
            @RequestParam(defaultValue = "5", required = false)
            Integer pageSize,
            @RequestParam(defaultValue = "0", required = false)
            Integer page

    ) {

        Pageable paging = PageRequest.of(page, pageSize);

        List<DatabaseResponse> databaseResponses =
                databaseService.getAllDatabases(paging);

        return new ResponseEntity<>(
                databaseResponses, HttpStatus.CREATED);
    }

    @PutMapping("/{databaseId}/server/link/{serverId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DatabaseResponse> addServerToDb(@PathVariable Integer databaseId, @PathVariable Integer serverId, @RequestBody DatabaseRequest databaseRequest) {
        DatabaseResponse updatedDatabase = databaseService.addServerToDb(databaseId, serverId, databaseRequest);
        return ResponseEntity.ok(updatedDatabase);
    }

    @PutMapping("/{databaseId}/server/unlink/{serverId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DatabaseResponse> removeServerFromDb(@PathVariable Integer databaseId, @PathVariable Integer serverId) {
        DatabaseResponse updatedDatabase = databaseService.removeServerFromDb(databaseId, serverId);
        return ResponseEntity.ok(updatedDatabase);
    }

    @GetMapping("/archived")
    public ResponseEntity<List<DatabaseResponse>> getAllArchivedDatabases(
            @RequestParam(defaultValue = "5", required = false)
            Integer pageSize,
            @RequestParam(defaultValue = "0", required = false)
            Integer page

    ) {

        Pageable paging = PageRequest.of(page, pageSize);

        List<DatabaseResponse> databaseResponses =
                databaseService.getAllArchivedDatabases(paging);

        return new ResponseEntity<>(
                databaseResponses, HttpStatus.CREATED);
    }

    @GetMapping("/{databaseId}/servers")
    public List<Server> getNonArchivedDatabaseServers(@PathVariable Integer databaseId) {
        return databaseService.getNonArchivedDatabaseServers(databaseId);
    }

    @GetMapping("/non-archived")
    public ResponseEntity<List<DatabaseResponse>> getAllNonArchivedDatabase(
            @RequestParam(defaultValue = "5", required = false)
            Integer pageSize,
            @RequestParam(defaultValue = "0", required = false)
            Integer page

    ) {

        Pageable paging = PageRequest.of(page, pageSize);

        List<DatabaseResponse> databaseResponses =
                databaseService.getAllNonArchivedDatabases(paging);

        return new ResponseEntity<>(
                databaseResponses, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}/hard")
    public ResponseEntity<DatabaseResponse> deleteDatabaseById(@PathVariable Integer id) {
        databaseService.deleteDatabaseById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DatabaseResponse> updateDatabase(@PathVariable Integer id, @RequestBody DatabaseRequest databaseRequest) {
        DatabaseResponse updatedDatabase = databaseService.updateDatabase(id, databaseRequest);
        return ResponseEntity.ok(updatedDatabase);
    }
    @GetMapping("/all")
    public List<DatabaseResponse> getAllDatabases(){
        return databaseService.getAllDatabases();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDatabaseSoft(@PathVariable Integer id) {
        databaseService.deleteDatabaseSoft(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DatabaseResponse findDatabaseById(@PathVariable Integer id) {
        return databaseService.findById(id);
    }


}
