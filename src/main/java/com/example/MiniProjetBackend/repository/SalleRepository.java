package com.example.MiniProjetBackend.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.MiniProjetBackend.entity.Salle;

public interface SalleRepository extends JpaRepository<Salle, Long> {

	Page<Salle> findByNomContainingIgnoreCase(String keyword, Pageable pageable);

    boolean existsByNomIgnoreCase(String nom);
}
