package com.example.MiniProjetBackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.MiniProjetBackend.entity.Cours;

public interface CoursRepository extends JpaRepository<Cours, String> {
	@Query("select f.nom, count(c) from Cours c join c.formateur f group by f.nom")
    List<Object[]> countCoursParFormateur();
    Page<Cours> findByTitreContainingIgnoreCase(String keyword, Pageable pageable);
    List<Cours> findByFormateur_Id(Long formateurId);
    Optional<Cours> findByCodeAndFormateur_Id(String code, Long formateurId);
}

