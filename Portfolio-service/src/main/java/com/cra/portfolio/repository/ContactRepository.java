package com.cra.portfolio.repository;

import com.cra.portfolio.model.Application;
import com.cra.portfolio.model.Contact;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
    List<Contact> findAllByDeletedAtNull(Pageable paging);
    List<Contact> findAllByDeletedAtNull();


    List<Contact> findAllByDeletedAtIsNotNull(Pageable paging);
}
