package co.edu.uniandes.ownerservice.service;

import co.edu.uniandes.ownerservice.model.Owner;
import co.edu.uniandes.ownerservice.model.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    @Autowired
    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Page<Owner> findByLastName(String lastName, Pageable pageable) {
        return ownerRepository.findByLastName(lastName, pageable);
    }

    public Optional<Owner> findById(Integer id) {
        return Optional.ofNullable(ownerRepository.findById(id));
    }

    public void save(Owner owner) {
        ownerRepository.save(owner);
    }

    public Page<Owner> findAll(Pageable pageable) {
        return ownerRepository.findAll(pageable);
    }
}