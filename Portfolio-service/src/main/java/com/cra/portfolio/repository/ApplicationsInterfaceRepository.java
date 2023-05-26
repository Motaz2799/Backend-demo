package com.cra.portfolio.repository;

import com.cra.portfolio.model.ApplicationsInterface;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationsInterfaceRepository extends JpaRepository<ApplicationsInterface, Integer> {

    /*@Transactional
    default ApplicationsInterface merge(ApplicationsInterface applicationsInterface) {
        return this.save(applicationsInterface);
    }*/


       /* @Modifying
        @Query("update ApplicationsInterface ai set ai.applicationSrc.id = :newSrcId, ai.applicationTarget.id = :newTargetId where ai.id.applicationSrcId = :srcId and ai.id.applicationTargetId = :targetId")
        int updateAppInterface(@Param("srcId") Integer srcId, @Param("targetId") Integer targetId, @Param("newSrcId") Integer newSrcId, @Param("newTargetId") Integer newTargetId);*/

List<ApplicationsInterface>findAllByDeletedAtNull(Pageable paging);
}
