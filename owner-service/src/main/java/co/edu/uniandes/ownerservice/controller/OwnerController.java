package co.edu.uniandes.ownerservice.controller;

import co.edu.uniandes.ownerservice.model.OwnerModel;
import co.edu.uniandes.ownerservice.model.OwnerModelAssembler;
import co.edu.uniandes.ownerservice.service.OwnerService;
import co.edu.uniandes.ownerservice.model.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/owners")
@CrossOrigin(origins = "*") // Permitir CORS para todos los orígenes
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private OwnerModelAssembler ownerModelAssembler;

    @GetMapping("/{id}")
    public ResponseEntity<OwnerModel> getOwnerById(@PathVariable Integer id) {
        return ownerService.findById(id)
                .map(ownerModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<PagedModel<OwnerModel>> getOwners(Pageable pageable) {
        Page<Owner> ownersPage = ownerService.findAll(pageable);
        List<OwnerModel> ownerModels = ownersPage.getContent().stream()
                .map(ownerModelAssembler::toModel)
                .collect(Collectors.toList());
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                ownersPage.getSize(), ownersPage.getNumber(), ownersPage.getTotalElements(), ownersPage.getTotalPages());
        PagedModel<OwnerModel> pagedModel = PagedModel.of(ownerModels, metadata);
        return ResponseEntity.ok(pagedModel);
    }

    @PostMapping
    public ResponseEntity<OwnerModel> createOwner(@RequestBody Owner owner) {
        ownerService.save(owner);
        return ResponseEntity.ok(ownerModelAssembler.toModel(owner));
    }

    @GetMapping("/search")
    public ResponseEntity<PagedModel<OwnerModel>> searchOwners(@RequestParam String lastName, Pageable pageable) {
        Page<Owner> ownersPage = ownerService.findByLastName(lastName, pageable);
        List<OwnerModel> ownerModels = ownersPage.getContent().stream()
                .map(ownerModelAssembler::toModel)
                .collect(Collectors.toList());
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                ownersPage.getSize(), ownersPage.getNumber(), ownersPage.getTotalElements(), ownersPage.getTotalPages());
        PagedModel<OwnerModel> pagedModel = PagedModel.of(ownerModels, metadata);
        return ResponseEntity.ok(pagedModel);
    }
}
