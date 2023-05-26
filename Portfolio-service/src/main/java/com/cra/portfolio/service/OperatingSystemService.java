package com.cra.portfolio.service;

import com.cra.portfolio.dto.ApplicationRequest;
import com.cra.portfolio.model.Application;
import com.cra.portfolio.model.OperatingSystem;
import com.cra.portfolio.repository.OperatingSystemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OperatingSystemService {

    private final OperatingSystemRepository OsRepository;

    public List<OperatingSystem> getAllOS() {
        return OsRepository.findAll();
    }

    public Optional<OperatingSystem> getOsById(Integer id) {
        return OsRepository.findById(id);
    }

    public OperatingSystem createOs(OperatingSystem Os) {
        return OsRepository.save(Os);
    }


}
