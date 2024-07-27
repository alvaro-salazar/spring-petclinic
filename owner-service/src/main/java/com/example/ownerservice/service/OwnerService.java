package com.example.ownerservice.service;

import com.example.ownerservice.model.Owner;
import com.example.ownerservice.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {
    @Autowired
    private OwnerRepository ownerRepository;

    public List<Owner> getAllOwners() {
        return ownerRepository.findAll();
    }

    public Optional<Owner> getOwnerById(Long id) {
        return ownerRepository.findById(id);
    }

    public List<Owner> getOwnersByLastName(String name){
        return ownerRepository.findByLastName(name);
    }

    public void createOwner(Owner owner) {
        ownerRepository.save(owner);
    }
}
