package com.example.MiniProjetBackend.repository;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.MiniProjetBackend.entity.Seance;


public interface SeanceRepository extends JpaRepository<Seance, Long> {

    Page<Seance> findBySalle_NomContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Seance> findByCours_CodeContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Seance> findByGroupe_Id(Long groupeId, Pageable pageable);
    List<Seance> findByGroupe_Id(Long groupeId);
    List<Seance> findByGroupe_IdOrderByDateAscHeureDebutAsc(Long groupeId);

    List<Seance> findByGroupe_IdAndDateBetweenOrderByDateAscHeureDebutAsc(
        Long groupeId, LocalDate start, LocalDate end
    );
    List<Seance> findByCours_Formateur_IdOrderByDateAscHeureDebutAsc(Long formateurId);

    List<Seance> findByCours_Formateur_IdAndDateBetweenOrderByDateAscHeureDebutAsc(
        Long formateurId, LocalDate from, LocalDate to
    );

    @Query("""
            select s from Seance s
            left join fetch s.cours
            left join fetch s.salle
            left join fetch s.groupe
            """)
        List<Seance> findAllWithDetails();
    
    
    List<Seance> findByCours_CodeAndCours_Formateur_IdOrderByDateAscHeureDebutAsc(String code, Long formateurId);

    List<Seance> findByCours_CodeAndCours_Formateur_IdAndDateBetweenOrderByDateAscHeureDebutAsc(
      String code, Long formateurId, LocalDate from, LocalDate to
    );

    // pour planning filtré (optionnel)
    List<Seance> findByCours_Formateur_IdAndCours_CodeAndGroupe_IdAndSalle_IdAndDateBetweenOrderByDateAscHeureDebutAsc(
      Long formateurId, String coursCode, Long groupeId, Long salleId, LocalDate from, LocalDate to
    );
    boolean existsByCours_CodeAndGroupe_Id(String coursCode, Long groupeId);


}

