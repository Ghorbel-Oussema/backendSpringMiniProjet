package com.example.MiniProjetBackend.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.MiniProjetBackend.dto.CoursDto;
import com.example.MiniProjetBackend.entity.Cours;
import com.example.MiniProjetBackend.entity.Seance;
import com.example.MiniProjetBackend.repository.CoursRepository;


@Service
public class CoursService {

    private final CoursRepository coursRepository;

    public CoursService(CoursRepository coursRepository) {
        this.coursRepository = coursRepository;
    }

    public Page<Cours> search(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return coursRepository.findAll(pageable);
        }
        return coursRepository.findByTitreContainingIgnoreCase(keyword, pageable);
    }

    public List<Cours> findAll() {
        return coursRepository.findAll();
    }
    public Cours findById(String id) {
        return coursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seance introuvable"));
    }

    public Cours save(Cours cours) {
        return coursRepository.save(cours);
    }

    public Cours findByCode(String code) {
        return coursRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Cours introuvable"));
    }

    public void delete(String code) {
        coursRepository.deleteById(code);
    }

    // ===== CSR DTO =====
    public List<CoursDto> findAllDto() {
        return coursRepository.findAll()
                .stream()
                .map(c -> {
                    CoursDto dto = new CoursDto();
                    dto.setCode(c.getCode());
                    dto.setTitre(c.getTitre());
                    dto.setDescription(c.getDescription());

                    if (c.getFormateur() != null) {
                        dto.setFormateurNom(c.getFormateur().getNom());
                        dto.setSpecialiteNom(
                            c.getFormateur().getSpecialite() != null
                                ? c.getFormateur().getSpecialite().getNom()
                                : null
                        );
                    }
                    return dto;
                })
                .toList();
    }
}
