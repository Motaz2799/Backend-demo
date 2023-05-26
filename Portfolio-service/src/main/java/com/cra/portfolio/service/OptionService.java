package com.cra.portfolio.service;

import com.cra.portfolio.model.OperatingSystem;
import com.cra.portfolio.model.Option;
import com.cra.portfolio.model.Question;
import com.cra.portfolio.repository.OptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OptionService {
    private final OptionRepository optionRepository;
    public void createOption(Option option) {
         option.setCreatedAt(LocalDateTime.now());
         optionRepository.save(option);
    }


}
