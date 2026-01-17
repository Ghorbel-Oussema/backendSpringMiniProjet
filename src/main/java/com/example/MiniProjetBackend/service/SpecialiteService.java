package com.example.MiniProjetBackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.MiniProjetBackend.entity.Cours;
import com.example.MiniProjetBackend.entity.Etudiant;
import com.example.MiniProjetBackend.entity.Specialite;
import com.example.MiniProjetBackend.repository.SpecialiteRepository;

@Service
public class SpecialiteService {

    private final SpecialiteRepository repo;

    public SpecialiteService(SpecialiteRepository repo) {
        this.repo = repo;
    }

    public Page<Specialite> search(String keyword, Pageable pageable) {
        return repo.findByNomContaining(keyword, pageable);
    }
    public List<Specialite> findAll() {
        return repo.findAll();
    }

    public Specialite save(Specialite s) {
        return repo.save(s);
    }

    public Specialite findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
