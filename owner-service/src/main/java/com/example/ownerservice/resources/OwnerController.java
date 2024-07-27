package com.example.ownerservice.resources;

import com.example.ownerservice.model.Owner;
import com.example.ownerservice.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OwnerController.class);

    @Autowired
    private OwnerService ownerService;

    @CrossOrigin(origins = "*")
    @GetMapping
    public List<Owner> getAll() {
        LOGGER.info("... Trying to get all owners ...");
        return ownerService.getAllOwners();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public ResponseEntity<Owner> getOwnerByID(@PathVariable Long id) {
        LOGGER.info("... Trying to get owner by id ...");
        Optional<Owner> owner = ownerService.getOwnerById(id);
        if (owner.isPresent()) {
            return new ResponseEntity<>(owner.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping
    public ResponseEntity<Owner> createOwner(@RequestBody Owner owner) {
        LOGGER.info("... Trying to save a new owner ...");
        if (owner.getFirstName().isEmpty() || owner.getLastName().isEmpty()|| owner.getCity().isEmpty() || owner.getAddress().isEmpty() || owner.getTelephone().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Owner> listOwnerByName = ownerService.getOwnersByLastName(owner.getLastName());
        if (!listOwnerByName.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ownerService.createOwner(owner);

        return new ResponseEntity<>(owner, HttpStatus.CREATED);
    }
}
