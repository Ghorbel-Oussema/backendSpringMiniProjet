package com.example.MiniProjetBackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.MiniProjetBackend.entity.Cours;
import com.example.MiniProjetBackend.entity.Note;
import com.example.MiniProjetBackend.repository.NoteRepository;

@Service
public class NoteService {

    @Autowired
    private NoteRepository repo;

    // ===== PAGINATION =====
    public Page<Note> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    // ===== GET ONE =====
    public Note findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // ===== SAVE =====
    public void save(Note n) {
        repo.save(n);
    }
    public Note savee(Note n) {
        return repo.save(n);
    }

    // ===== DELETE =====
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ===== FILTERS =====
    public List<Note> notesParEtudiant(String matricule) {
        return repo.findByEtudiantMatricule(matricule);
    }

    public List<Note> notesParCours(String code) {
        return repo.findByCoursCode(code);
    }
}
