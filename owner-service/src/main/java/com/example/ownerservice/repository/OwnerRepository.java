package com.example.ownerservice.repository;

import com.example.ownerservice.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    List<Owner> findByLastName(String lastName);
}