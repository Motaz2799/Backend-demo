package com.cra.portfolio.dto;

import com.cra.portfolio.enums.Flow;
import com.cra.portfolio.enums.Frequency;
import com.cra.portfolio.enums.ProcessingMode;
import com.cra.portfolio.model.Application;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApplicationInterfaceResponse {
    private Integer id;
    private Application applicationSrc;
    private Application applicationTarget;
    private String protocol;
    private String dataFormat;
    private String notes;
    private Flow flow;
    private Frequency frequency;
    private ProcessingMode processingMode;
    private LocalDateTime deletedAt = null;
    private LocalDateTime modifiedAt = null;
    private LocalDateTime createdAt = null;


}
