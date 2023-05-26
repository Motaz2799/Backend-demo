package com.cra.portfolio.controller;

import com.cra.portfolio.model.OperatingSystem;
import com.cra.portfolio.service.OperatingSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/os")
@CrossOrigin("*")
public class OperatingSystemController {
    @Autowired
    private OperatingSystemService operatingSystemService;

    @PostMapping
    public ResponseEntity<OperatingSystem> createServer(@RequestBody OperatingSystem Os) {
        OperatingSystem newOs = operatingSystemService.createOs(Os);
        return new ResponseEntity<>(newOs, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OperatingSystem>> getAllOperatingSystem() {
        List<OperatingSystem> Os = operatingSystemService.getAllOS();
        return new ResponseEntity<>(Os, HttpStatus.OK);
    }


}
