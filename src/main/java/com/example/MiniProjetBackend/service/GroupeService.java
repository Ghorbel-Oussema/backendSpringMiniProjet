package com.example.MiniProjetBackend.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.MiniProjetBackend.entity.Etudiant;
import com.example.MiniProjetBackend.entity.Groupe;
import com.example.MiniProjetBackend.repository.GroupeRepository;

@Service
public class GroupeService {

    private final GroupeRepository repo;

    public GroupeService(GroupeRepository repo) {
        this.repo = repo;
    }

    public Page<Groupe> search(String keyword, Pageable pageable) {
        return repo.findByNomContainingIgnoreCase(keyword, pageable);
    }

    public Groupe findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Groupe introuvable"));
    }

    public void save(Groupe g) {
        repo.save(g);
    }
    public Groupe savee(Groupe g) {
        return repo.save(g);
    }
    public List<Groupe> findAll() {
        return repo.findAll();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public void addEtudiants(Long id, List<Etudiant> etudiants) {
        Groupe g = findById(id);
        g.setEtudiants(etudiants);
        repo.save(g);
    }

    
}
