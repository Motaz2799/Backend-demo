package com.cra.portfolio.controller;

import com.cra.portfolio.dto.ContactRequest;
import com.cra.portfolio.dto.ContactResponse;
import com.cra.portfolio.dto.DatabaseResponse;
import com.cra.portfolio.model.Application;
import com.cra.portfolio.model.Server;
import com.cra.portfolio.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contacts")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactResponse createContact(@RequestBody ContactRequest contactRequest) {
        return contactService.createContact(contactRequest);
    }

    @GetMapping
    public ResponseEntity<List<ContactResponse>> getAllContacts(
            @RequestParam(defaultValue = "5", required = false)
            Integer pageSize,
            @RequestParam(defaultValue = "0", required = false)
            Integer page

    ) {

        Pageable paging = PageRequest.of(page, pageSize);

        List<ContactResponse> contactResponses =
                contactService.getAllContact(paging);

        return new ResponseEntity<>(
                contactResponses, HttpStatus.CREATED);
    }


    @PutMapping("/{contactId}/application/link/{applicationId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ContactResponse> addAppToContact(@PathVariable Integer applicationId, @PathVariable Integer contactId, @RequestBody ContactRequest contactRequest) {
        ContactResponse updatedContact = contactService.addAppToContact(contactId, applicationId, contactRequest);
        return ResponseEntity.ok(updatedContact);
    }


    @PutMapping("/{contactId}/application/unlink/{applicationId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ContactResponse> removeAppFromContact(@PathVariable Integer applicationId, @PathVariable Integer contactId) {
        ContactResponse updatedContact = contactService.removeAppFromContact(contactId, applicationId);
        return ResponseEntity.ok(updatedContact);
    }

    @GetMapping("/{contactId}/applications")
    public List<Application> getNonArchivedContactApplications(@PathVariable Integer contactId) {
        return contactService.getNonArchivedContactApplications(contactId);
    }

    @GetMapping("/archived")
    public ResponseEntity<List<ContactResponse>> getAllArchivedContacts(
            @RequestParam(defaultValue = "5", required = false)
            Integer pageSize,
            @RequestParam(defaultValue = "0", required = false)
            Integer page

    ) {

        Pageable paging = PageRequest.of(page, pageSize);

        List<ContactResponse> contactResponses =
                contactService.getAllArchivedContacts(paging);

        return new ResponseEntity<>(
                contactResponses, HttpStatus.CREATED);
    }

    @GetMapping("/non-archived")
    public ResponseEntity<List<ContactResponse>> getAllNonArchivedContacts(
            @RequestParam(defaultValue = "5", required = false)
            Integer pageSize,
            @RequestParam(defaultValue = "0", required = false)
            Integer page

    ) {

        Pageable paging = PageRequest.of(page, pageSize);

        List<ContactResponse> contactResponses =
                contactService.getAllNonArchivedContacts(paging);

        return new ResponseEntity<>(
                contactResponses, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<ContactResponse> deleteContactById(@PathVariable Integer id) {
        contactService.deleteContactById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/all")
    public List<ContactResponse> getAllNonPageableContact(){
        return contactService.getAllNonPageableContact();
    }


    @PutMapping("/{id}")
    public ResponseEntity<ContactResponse> updateContact(@PathVariable Integer id, @RequestBody ContactRequest contactRequest) {
        ContactResponse updatedContact = contactService.updateContact(id, contactRequest);
        return ResponseEntity.ok(updatedContact);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactSoft(@PathVariable Integer id) {
        contactService.deleteContactSoft(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ContactResponse findContactById(@PathVariable Integer id) {
        return contactService.findById(id);
    }

}


