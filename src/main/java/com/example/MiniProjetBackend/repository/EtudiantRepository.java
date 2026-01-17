package com.example.MiniProjetBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.MiniProjetBackend.entity.Etudiant;
import com.example.MiniProjetBackend.entity.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface EtudiantRepository extends JpaRepository<Etudiant, String> {

    Page<Etudiant> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(
            String nom, String prenom, Pageable pageable);
    Optional<Etudiant> findByUser(User user);
    @Query("""
            select e from Etudiant e
            left join fetch e.user
            left join fetch e.groupe
            where e.matricule = :m
        """)
        Optional<Etudiant> findByMatriculeWithUserAndGroupe(@Param("m") String matricule);
    Optional<Etudiant> findByUser_Email(String email);

    List<Etudiant> findByGroupe_IdOrderByNomAscPrenomAsc(Long groupeId);

}


