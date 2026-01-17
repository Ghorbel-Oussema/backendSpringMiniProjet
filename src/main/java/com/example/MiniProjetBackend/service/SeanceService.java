package com.example.MiniProjetBackend.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.MiniProjetBackend.dto.SeanceDetailsDTO;
import com.example.MiniProjetBackend.entity.Seance;
import com.example.MiniProjetBackend.repository.SeanceRepository;

@Service
public class SeanceService {

    @Autowired
    private SeanceRepository repo;
    public List<SeanceDetailsDTO> findAllWithDetails() {
        List<Seance> seances = repo.findAllWithDetails();

        return seances.stream().map(s -> {
            SeanceDetailsDTO dto = new SeanceDetailsDTO();
            dto.id = s.getId();
            dto.date = s.getDate();
            dto.heureDebut = s.getHeureDebut();
            dto.heureFin = s.getHeureFin();

            if (s.getCours() != null) {
                dto.cours = new SeanceDetailsDTO.CoursDTO(
                    s.getCours().getCode(),
                    s.getCours().getTitre() // adapte si pas "titre"
                );
            }

            if (s.getSalle() != null) {
                dto.salle = new SeanceDetailsDTO.SalleDTO(
                    s.getSalle().getId(),
                    s.getSalle().getNom(),
                    s.getSalle().getCapacite()
                );
            }

            if (s.getGroupe() != null) {
                dto.groupe = new SeanceDetailsDTO.GroupeDTO(
                    s.getGroupe().getId(),
                    s.getGroupe().getNom() // adapte si "libelle"
                );
            }

            return dto;
        }).collect(Collectors.toList());
    }

    // ✅ SEARCH + PAGINATION (par nom salle)
    public Page<Seance> search(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return repo.findAll(pageable);
        }
        return repo.findBySalle_NomContainingIgnoreCase(keyword.trim(), pageable);
    }

    public boolean conflit(LocalDate date, LocalTime debut, LocalTime fin) {
        // TODO: vraie logique de conflit
        return false;
    }

    public void save(Seance s) {
        if (conflit(s.getDate(), s.getHeureDebut(), s.getHeureFin())) {
            throw new RuntimeException("Conflit horaire");
        }
        repo.save(s);
    }

    public Seance findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Seance introuvable"));
    }

    public List<Seance> findAll() {
        return repo.findAll();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
    public Page<Seance> findByGroupe(Long groupeId, Pageable pageable) {
        return repo.findByGroupe_Id(groupeId, pageable);
    }

    public List<Seance> findByGroupe(Long groupeId) {
        return repo.findByGroupe_Id(groupeId);
    }

}
