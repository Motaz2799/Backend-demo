package com.cra.portfolio.controller;

import com.cra.portfolio.dto.*;
import com.cra.portfolio.service.ApplicationsInterfaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/interfaces")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ApplicationsInterfaceController {
    private final ApplicationsInterfaceService applicationsInterfaceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationInterfaceResponse createApplicationsInterface(@RequestBody ApplicationInterfaceRequest applicationRequest) {
        return (applicationsInterfaceService.createAppInterface(applicationRequest));
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ApplicationInterfaceResponse> getAllApplicationsInterface() {
        return applicationsInterfaceService.getAllAppInterfaces();
    }

    @PutMapping("/{id}")
    public ApplicationInterfaceResponse updateAppInterface(@PathVariable Integer id,
                                                           @RequestBody ApplicationInterfaceRequest updatedInterface) {
        return applicationsInterfaceService.updateAppInterface(id, updatedInterface);

    }

    @GetMapping("/{id}")
    public ApplicationInterfaceResponse getApplicationInterfaceById(@PathVariable Integer id) {
        return applicationsInterfaceService.getAppInterfaceById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteApplicationInterface(@PathVariable Integer id) {
        applicationsInterfaceService.deleteAppInterface(id);
    }

    @GetMapping("/non-archived")
    public ResponseEntity<List<ApplicationInterfaceResponse>> getAllNonArchivedAppInterfaces(
            @RequestParam(defaultValue = "5", required = false)
            Integer pageSize,
            @RequestParam(defaultValue = "0", required = false)
            Integer page

    ) {

        Pageable paging = PageRequest.of(page, pageSize);

        List<ApplicationInterfaceResponse> interfaceResponses =
                applicationsInterfaceService.getAllNonArchivedInterfaces(paging);

        return new ResponseEntity<>(
                interfaceResponses, HttpStatus.CREATED);
    }


}
