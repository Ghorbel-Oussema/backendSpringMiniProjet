package com.example.MiniProjetBackend.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.MiniProjetBackend.entity.Specialite;


public interface SpecialiteRepository
extends JpaRepository<Specialite, Long> {
Page<Specialite> findByNomContaining(String keyword, Pageable pageable);

@Query("""
        select s.nom, count(c)
        from Specialite s
        left join s.formateurs f
        left join f.cours c
        group by s.nom
    """)
    List<Object[]> countCoursParSpecialite();
}
