package com.cra.portfolio.service;

import com.cra.portfolio.dto.*;
import com.cra.portfolio.exception.NotFoundCustomException;
import com.cra.portfolio.model.Application;
import com.cra.portfolio.model.Contact;
import com.cra.portfolio.model.Database;
import com.cra.portfolio.model.Server;
import com.cra.portfolio.repository.ApplicationRepository;
import com.cra.portfolio.repository.ContactRepository;
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
public class ContactService {
    private final ApplicationRepository applicationRepository;
    private final ContactRepository contactRepository;


    public ContactResponse createContact(ContactRequest contactRequest) {

        Contact contact = Contact.builder()
                .fullName(contactRequest.getFullName())
                .title(contactRequest.getTitle())
                .createdAt(LocalDateTime.now())
                .department(contactRequest.getDepartment())
                .email(contactRequest.getEmail())
                .build();
        contactRepository.save(contact);
        log.info("Contact {} created successfully", contact.getId());
        return mapToContactResponse(contact);
    }

    public ContactResponse addAppToContact(Integer contactId, Integer applicationId, ContactRequest contactRequest) {
        Optional<Contact> optionalContact = contactRepository.findById(contactId);
        if (optionalContact.isPresent()) {
            Contact contact = optionalContact.get();
            Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new NotFoundCustomException("Application not found with id: " + applicationId));
            if (application.getDeletedAt() != null) {
                throw new NotFoundCustomException("Application not found with id: " + applicationId);
            }
            List<Application> applications = contact.getApplications();
            if (!applications.contains(application)) {
                applications.add(application);
                application.getContacts().add(contact);
                contact.setApplications(applications);
                contact.setFullName(contactRequest.getFullName());
                contact.setTitle(contactRequest.getTitle());
                contact.setDepartment(contactRequest.getDepartment());
                contact.setEmail(contactRequest.getEmail());
                contact.setModifiedAt(LocalDateTime.now());

                Contact updatedContact = contactRepository.save(contact);
                return mapToContactResponse(updatedContact);
            } else {
                return mapToContactResponse(contact);
            }
        } else {
            throw new NotFoundCustomException("Contact not found with id: " + contactId);
        }
    }


    public List<Application> getNonArchivedContactApplications(Integer contactId) {
        Optional<Contact> optionalContact = contactRepository.findById(contactId);
        if (optionalContact.isPresent()) {
            Contact contact = optionalContact.get();
            List<Application> applications = new ArrayList<>();
            for (Application application : contact.getApplications()) {
                for (Contact appContact : application.getContacts()) {
                    if (appContact.getId().equals(contact.getId()) && application.getDeletedAt() == null) {
                        applications.add(application);
                        break;
                    }
                }
            }
            return applications;
        } else {
            throw new NotFoundCustomException("Contact not found with id: " + contactId);
        }
    }

    public ContactResponse removeAppFromContact(Integer contactId, Integer applicationId) {
        Optional<Contact> optionalContact = contactRepository.findById(contactId);
        if (optionalContact.isPresent()) {
            Contact contact = optionalContact.get();
            Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new NotFoundCustomException("Application not found with id: " + applicationId));

            List<Application> applications = contact.getApplications();
            if (applications.contains(application)) {
                applications.remove(application);
                application.removeContact(contact);

                contact.setApplications(applications);
                Contact updatedContact = contactRepository.save(contact);
                return mapToContactResponse(updatedContact);
            } else {
                return mapToContactResponse(contact);
            }
        } else {
            throw new NotFoundCustomException("Contact not found with id: " + contactId);
        }
    }

    public List<ContactResponse> getAllContact(Pageable paging) {

        Iterable<Contact> contacts = contactRepository.findAll(paging);

        List<ContactResponse> contactResponses = new ArrayList<>();

        contacts.forEach(contact ->
                contactResponses.add(ContactResponse
                        .builder()
                        .id(contact.getId())
                        .fullName(contact.getFullName())
                        .department(contact.getDepartment())
                        .title(contact.getTitle())
                        .email(contact.getEmail())
                        .applications(contact.getApplications())
                        .modifiedAt(contact.getModifiedAt())
                        .deletedAt(contact.getDeletedAt())
                        .createdAt(contact.getCreatedAt())
                        .build())
        );

        return contactResponses;
    }

    public void deleteContactById(Integer id) {
        Optional<Contact> optionalContact = contactRepository.findById(id);
        if (optionalContact.isPresent()) {
            Contact contact = optionalContact.get();
            for (Application application : contact.getApplications()) {
                application.getContacts().remove(contact);
            }
            contactRepository.delete(optionalContact.get());
        } else {
            throw new NotFoundCustomException("Contact not found with id: " + id);
        }
    }

    public ContactResponse updateContact(Integer id, ContactRequest contactRequest) {
        Optional<Contact> optionalContact = contactRepository.findById(id);
        if (optionalContact.isPresent()) {
            LocalDateTime modified = LocalDateTime.now();
            Contact contact = optionalContact.get();
            contact.setFullName(contactRequest.getFullName());
            contact.setDepartment(contactRequest.getDepartment());
            contact.setTitle(contactRequest.getTitle());
            contact.setEmail(contactRequest.getEmail());
            contact.setModifiedAt((modified));

            Contact updatedContact = contactRepository.save(contact);

            return mapToContactResponse(updatedContact);
        } else {
            throw new NotFoundCustomException("Contact not found with id: " + id);
        }
    }

    public void deleteContactSoft(Integer id) {
        Optional<Contact> optionalContact = contactRepository.findById(id);
        LocalDateTime deleted = LocalDateTime.now();
        if (optionalContact.isPresent()) {
            Contact contact = optionalContact.get();
            contact.setDeletedAt(deleted);
            contactRepository.save(contact);
            log.info("server {} archived successfully", contact.getId());
        } else {
            throw new NotFoundCustomException("Contact not found with id: " + id);
        }


    }


    public List<ContactResponse> getAllNonArchivedContacts(Pageable paging) {

        Iterable<Contact> contacts = contactRepository.findAllByDeletedAtNull(paging);

        List<ContactResponse> contactResponses = new ArrayList<>();

        contacts.forEach(contact ->
                contactResponses.add(ContactResponse
                        .builder()
                        .applications(contact.getApplications())
                        .id(contact.getId())
                        .fullName(contact.getFullName())
                        .department(contact.getDepartment())
                        .title(contact.getTitle())
                        .email(contact.getEmail())
                        .applications(contact.getApplications())
                        .modifiedAt(contact.getModifiedAt())
                        .deletedAt(contact.getDeletedAt())
                        .createdAt(contact.getCreatedAt())
                        .build())
        );

        return contactResponses;
    }

    public List<ContactResponse> getAllArchivedContacts(Pageable paging) {

        Iterable<Contact> contacts = contactRepository.findAllByDeletedAtIsNotNull(paging);

        List<ContactResponse> contactResponses = new ArrayList<>();

        contacts.forEach(contact ->
                contactResponses.add(ContactResponse
                        .builder()
                        .applications(contact.getApplications())
                        .id(contact.getId())
                        .fullName(contact.getFullName())
                        .department(contact.getDepartment())
                        .title(contact.getTitle())
                        .email(contact.getEmail())
                        .applications(contact.getApplications())
                        .modifiedAt(contact.getModifiedAt())
                        .deletedAt(contact.getDeletedAt())
                        .createdAt(contact.getCreatedAt())
                        .build())
        );

        return contactResponses;
    }

    public ContactResponse findById(Integer id) {
        Optional<Contact> contact = contactRepository.findById(id);

        if (contact.isPresent()) {
            Contact cont = contact.get();
            if (cont.getDeletedAt() != null) {
                throw new NotFoundCustomException("Contact not found with id: " + id);
            }
            return ContactResponse
                    .builder()
                    .applications(cont.getApplications())
                    .id(cont.getId())
                    .fullName(cont.getFullName())
                    .department(cont.getDepartment())
                    .title(cont.getTitle())
                    .email(cont.getEmail())
                    .applications(cont.getApplications())
                    .modifiedAt(cont.getModifiedAt())
                    .deletedAt(cont.getDeletedAt())
                    .createdAt(cont.getCreatedAt())
                    .build();

        } else {
            throw new NotFoundCustomException("Contact not found with id: " + id);
        }

    }
    public List<ContactResponse> getAllNonPageableContact() {

        Iterable<Contact> contacts = contactRepository.findAll();

        List<ContactResponse> contactResponses = new ArrayList<>();

        contacts.forEach(contact ->
                contactResponses.add(ContactResponse
                        .builder()
                        .id(contact.getId())
                        .fullName(contact.getFullName())
                        .department(contact.getDepartment())
                        .title(contact.getTitle())
                        .email(contact.getEmail())
                        .applications(contact.getApplications())
                        .modifiedAt(contact.getModifiedAt())
                        .deletedAt(contact.getDeletedAt())
                        .createdAt(contact.getCreatedAt())
                        .build())
        );

        return contactResponses;
    }


    private ContactResponse mapToContactResponse(Contact contact) {
        return ContactResponse.builder().id(contact.getId())
                .fullName(contact.getFullName())
                .email(contact.getEmail())
                .title(contact.getTitle())
                .department(contact.getDepartment())
                .applications(contact.getApplications())
                .modifiedAt(contact.getModifiedAt())
                .deletedAt(contact.getDeletedAt())
                .createdAt(contact.getCreatedAt())
                .build();
    }


}
