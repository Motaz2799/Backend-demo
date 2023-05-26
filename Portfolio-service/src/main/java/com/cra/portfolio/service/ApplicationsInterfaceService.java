package com.cra.portfolio.service;

import com.cra.portfolio.dto.ApplicationInterfaceRequest;
import com.cra.portfolio.dto.ApplicationInterfaceResponse;
import com.cra.portfolio.exception.NotFoundCustomException;
import com.cra.portfolio.model.ApplicationsInterface;
import com.cra.portfolio.repository.ApplicationRepository;
import com.cra.portfolio.repository.ApplicationsInterfaceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationsInterfaceService {
    private final ApplicationRepository applicationRepository;
    private final ApplicationsInterfaceRepository applicationsInterfaceRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public ApplicationInterfaceResponse createAppInterface(ApplicationInterfaceRequest request) {


        ApplicationsInterface applicationsInterface = ApplicationsInterface.builder()
                .protocol(request.getProtocol())
                .applicationSrc(request.getApplicationSrc())
                .applicationTarget(request.getApplicationTarget())
                .dataFormat(request.getDataFormat())
                .notes(request.getNotes())
                .flow(request.getFlow())
                .frequency(request.getFrequency())
                .processingMode(request.getProcessingMode())
                .createdAt(LocalDateTime.now())
                .build();
        applicationsInterfaceRepository.save(applicationsInterface);
        return mapToAppInterfaceResponse(applicationsInterface);
    }

    public List<ApplicationInterfaceResponse> getAllAppInterfaces() {
        Iterable<ApplicationsInterface> applicationsInterfaces = applicationsInterfaceRepository.findAll();
        List<ApplicationInterfaceResponse> applicationInterfaceResponses = new ArrayList<>();
        applicationsInterfaces.forEach(applicationsInterface -> applicationInterfaceResponses.add(mapToAppInterfaceResponse(applicationsInterface)));
        return applicationInterfaceResponses;

    }

    public List<ApplicationInterfaceResponse>getAllNonArchivedInterfaces(Pageable paging){
        Iterable<ApplicationsInterface> appInterfaces = applicationsInterfaceRepository.findAllByDeletedAtNull(paging);
        List<ApplicationInterfaceResponse> applicationInterfaceResponses = new ArrayList<>();
        appInterfaces.forEach(applicationsInterface -> applicationInterfaceResponses.add(mapToAppInterfaceResponse(applicationsInterface)));
        return applicationInterfaceResponses;
    }


    @Transactional
    public ApplicationInterfaceResponse updateAppInterface(Integer id, ApplicationInterfaceRequest updatedInterface) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaUpdate<ApplicationsInterface> criteriaUpdate = cb.createCriteriaUpdate(ApplicationsInterface.class);
        Root<ApplicationsInterface> root = criteriaUpdate.from(ApplicationsInterface.class);
        criteriaUpdate.set(root.get("applicationSrc").get("id"), updatedInterface.getApplicationSrc().getId());
        criteriaUpdate.set(root.get("applicationTarget").get("id"), updatedInterface.getApplicationTarget().getId());
        criteriaUpdate.set(root.get("protocol"), updatedInterface.getProtocol());
        criteriaUpdate.set(root.get("dataFormat"), updatedInterface.getDataFormat());
        criteriaUpdate.set(root.get("notes"), updatedInterface.getNotes());
        criteriaUpdate.set(root.get("flow"), updatedInterface.getFlow());
        criteriaUpdate.set(root.get("frequency"), updatedInterface.getFrequency());
        criteriaUpdate.set(root.get("processingMode"), updatedInterface.getProcessingMode());
        criteriaUpdate.where(cb.equal(root.get("id"),id));


        int result = entityManager.createQuery(criteriaUpdate).executeUpdate();
        if (result == 1) {
ApplicationsInterface updatedEntity=entityManager.find(ApplicationsInterface.class,id);



            return mapToAppInterfaceResponse(updatedEntity);
        } else {
            throw new RuntimeException("Failed to update ApplicationsInterface record.");
        }
    }

    public ApplicationInterfaceResponse getAppInterfaceById(Integer id) {
        Optional<ApplicationsInterface> applicationsInterface = applicationsInterfaceRepository.findById(id);
        if (applicationsInterface.isPresent()) {
            return mapToAppInterfaceResponse(applicationsInterface.get());
        } else {
            throw new NotFoundCustomException("ApplicationsInterface not found.");
        }
    }

    public void deleteAppInterface(Integer id) {
        applicationsInterfaceRepository.deleteById(id);
    }






/*@Transactional
    public ApplicationInterfaceResponse updateAppInterface(Integer srcId, Integer targetId, ApplicationInterfaceRequest updatedInterface) {
        int result = applicationsInterfaceRepository.updateAppInterface(srcId, targetId, updatedInterface.getApplicationSrc().getId(), updatedInterface.getApplicationTarget().getId());
        if (result == 1) {
            return new ApplicationInterfaceResponse();
        } else {
            throw new RuntimeException("Failed to update ApplicationsInterface record.");
        }
    }*/

/*@Transactional
    public ApplicationInterfaceResponse updateAppInterface(Integer srcId ,Integer targetId, ApplicationInterfaceRequest request) {
        ApplicationsKey embeddedKey = new ApplicationsKey(srcId, targetId);
    ApplicationsInterface detachedAppInt = new ApplicationsInterface();



    detachedAppInt.setId(embeddedKey);
    detachedAppInt.setProtocol(request.getProtocol());
    detachedAppInt.setApplicationSrc(request.getApplicationSrc());
    detachedAppInt.setApplicationTarget(request.getApplicationTarget());
    detachedAppInt.setDataFormat(request.getDataFormat());
    detachedAppInt.setNotes(request.getNotes());
    detachedAppInt.setFlow(request.getFlow());
    detachedAppInt.setFrequency(request.getFrequency());
    detachedAppInt.setProcessingMode(request.getProcessingMode());
    detachedAppInt.getId().setApplicationSrcId(request.getApplicationSrc().getId());
    detachedAppInt.getId().setApplicationTargetId(request.getApplicationTarget().getId());
    ApplicationsInterface mergedAppInt = applicationsInterfaceRepository.merge(detachedAppInt);
            return mapToAppInterfaceResponse(mergedAppInt);





    } */


/*@Transactional
public ApplicationInterfaceResponse updateAppInterface(Integer srcId, Integer targetId, ApplicationInterfaceRequest request) {
    ApplicationsKey embeddedKey = new ApplicationsKey(srcId, targetId);
    Optional<ApplicationsInterface> optionalAppInt = applicationsInterfaceRepository.findById(embeddedKey);

    if (optionalAppInt.isPresent()) {
        ApplicationsInterface applicationsInterface = optionalAppInt.get();
        applicationsInterface.setProtocol(request.getProtocol());

        // Load the Application entities from the database
        Application srcApplication = applicationRepository.findById(request.getApplicationSrc().getId())
                .orElseThrow(() -> new NotFoundCustomException("Application not found"));
        Application targetApplication = applicationRepository.findById(request.getApplicationTarget().getId())
                .orElseThrow(() -> new NotFoundCustomException("Application not found"));

        // Set the loaded Application entities to the applicationsInterface entity
        applicationsInterface.setApplicationSrc(srcApplication);
        applicationsInterface.setApplicationTarget(targetApplication);

        applicationsInterface.setDataFormat(request.getDataFormat());
        applicationsInterface.setNotes(request.getNotes());
        applicationsInterface.setFlow(request.getFlow());
        applicationsInterface.setFrequency(request.getFrequency());
        applicationsInterface.setProcessingMode(request.getProcessingMode());
        applicationsInterface.getId().setApplicationSrcId(request.getApplicationSrc().getId());
        applicationsInterface.getId().setApplicationTargetId(request.getApplicationTarget().getId());

        applicationsInterfaceRepository.save(applicationsInterface);
        return mapToAppInterfaceResponse(applicationsInterface);

    } else {
        throw new NotFoundCustomException("Applications not found");
    }
}*/


    private ApplicationInterfaceResponse mapToAppInterfaceResponse(ApplicationsInterface applicationsInterface) {
        return ApplicationInterfaceResponse.builder()
                .id(applicationsInterface.getId())
                .applicationSrc(applicationsInterface.getApplicationSrc())
                .applicationTarget(applicationsInterface.getApplicationTarget())
                .protocol(applicationsInterface.getProtocol())
                .dataFormat(applicationsInterface.getDataFormat())
                .notes(applicationsInterface.getNotes())
                .flow(applicationsInterface.getFlow())
                .frequency(applicationsInterface.getFrequency())
                .processingMode(applicationsInterface.getProcessingMode())
                .createdAt(applicationsInterface.getCreatedAt())
                .deletedAt(applicationsInterface.getDeletedAt())
                .modifiedAt(applicationsInterface.getModifiedAt())
                .build();
    }

}
