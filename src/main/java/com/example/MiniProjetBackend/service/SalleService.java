package com.example.MiniProjetBackend.service;

import com.example.MiniProjetBackend.entity.Salle;
import com.example.MiniProjetBackend.entity.Seance;
import com.example.MiniProjetBackend.repository.SalleRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SalleService {

    private final SalleRepository salleRepository;

    public SalleService(SalleRepository salleRepository) {
        this.salleRepository = salleRepository;
    }

    public Page<Salle> getSallesPage(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);

        if (keyword == null || keyword.trim().isEmpty()) {
            return salleRepository.findAll(pageable);
        }
        return salleRepository.findByNomContainingIgnoreCase(keyword.trim(), pageable);
    }

    public Salle getSalleById(Long id) {
        return salleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salle introuvable : id=" + id));
    }
    public List<Salle> findAll() {
        return salleRepository.findAll();
    }

    public Salle saveOrUpdate(Salle salle) {
        // Optionnel: empêcher doublon nom lors de création
        if (salle.getId() == null && salle.getNom() != null
                && salleRepository.existsByNomIgnoreCase(salle.getNom())) {
            throw new RuntimeException("Salle existe déjà : " + salle.getNom());
        }
        return salleRepository.save(salle);
    }

    public void deleteSalle(Long id) {
        salleRepository.deleteById(id);
    }
}
