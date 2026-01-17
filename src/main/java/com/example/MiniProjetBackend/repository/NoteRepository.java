package com.example.MiniProjetBackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.MiniProjetBackend.entity.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByEtudiantMatricule(String matricule);

    List<Note> findByCoursCode(String code);
    List<Note> findByEtudiant_Matricule(String matricule);

    Optional<Note> findByEtudiant_MatriculeAndCours_Code(String matricule, String coursCode);
    List<Note> findByCours_CodeAndEtudiant_Groupe_Id(String coursCode, Long groupeId);

    List<Note> findByCours_Code(String coursCode);


    // ✅ moyenne cours
    @Query("select avg(n.valeur) from Note n where n.cours.code = :code")
    Double avgByCours(@Param("code") String code);

    // ✅ moyenne cours + groupe
    @Query("""
      select avg(n.valeur)
      from Note n
      where n.cours.code = :code
        and n.etudiant.groupe.id = :groupeId
    """)
    Double avgByCoursAndGroupe(@Param("code") String code, @Param("groupeId") Long groupeId);
}

