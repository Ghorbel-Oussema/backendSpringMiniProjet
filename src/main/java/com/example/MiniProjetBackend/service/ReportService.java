package com.example.MiniProjetBackend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.MiniProjetBackend.entity.Note;

@Service
public class ReportService {

    public double moyenneEtudiant(List<Note> notes) {
        return notes.stream()
                .mapToDouble(Note::getValeur)
                .average().orElse(0);
    }

    public void generatePdfMock() {
        System.out.println("PDF généré (simulation)");
    }
}
