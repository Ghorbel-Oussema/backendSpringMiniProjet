package com.example.MiniProjetBackend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.MiniProjetBackend.entity.Formateur;
import com.example.MiniProjetBackend.entity.User;

public interface FormateurRepository extends JpaRepository<Formateur, Long> {
    Page<Formateur> findByNomContainingIgnoreCase(String keyword, Pageable pageable);
    Optional<Formateur> findByUser(User user);
    @Query("""
    	    select f from Formateur f
    	    left join fetch f.user
    	    left join fetch f.specialite
    	    where f.id = :id
    	""")
    	Optional<Formateur> findByIdWithUserAndSpecialite(@Param("id") Long id);
    Optional<Formateur> findByUser_Email(String email);

	
}
