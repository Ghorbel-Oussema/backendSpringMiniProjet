package com.example.MiniProjetBackend.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.MiniProjetBackend.entity.Groupe;


public interface GroupeRepository
extends JpaRepository<Groupe, Long> {

	Page<Groupe> findByNomContainingIgnoreCase(
            String keyword, Pageable pageable);

    @Query("select g.nom, size(g.etudiants) from Groupe g")
    List<Object[]> countEtudiantsParGroupe();
}
