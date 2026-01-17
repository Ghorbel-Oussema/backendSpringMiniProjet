package com.example.MiniProjetBackend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.MiniProjetBackend.repository.*;

@Service
public class DashboardService {

    private final EtudiantRepository etudiantRepo;
    private final FormateurRepository formateurRepo;
    private final CoursRepository coursRepo;
    private final GroupeRepository groupeRepo;
    private final SpecialiteRepository specialiteRepo;
    private final SeanceRepository seanceRepo;
    private final NoteRepository noteRepo;

    public DashboardService(
            EtudiantRepository etudiantRepo,
            FormateurRepository formateurRepo,
            CoursRepository coursRepo,
            GroupeRepository groupeRepo,
            SpecialiteRepository specialiteRepo,
            SeanceRepository seanceRepo,
            NoteRepository noteRepo) {

        this.etudiantRepo = etudiantRepo;
        this.formateurRepo = formateurRepo;
        this.coursRepo = coursRepo;
        this.groupeRepo = groupeRepo;
        this.specialiteRepo = specialiteRepo;
        this.seanceRepo = seanceRepo;
        this.noteRepo = noteRepo;
    }

    public Map<String, Long> globalStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("etudiants", etudiantRepo.count());
        stats.put("formateurs", formateurRepo.count());
        stats.put("cours", coursRepo.count());
        stats.put("groupes", groupeRepo.count());
        stats.put("specialites", specialiteRepo.count());
        stats.put("seances", seanceRepo.count());
        stats.put("notes", noteRepo.count());
        return stats;
    }



    public List<Object[]> coursParFormateur() {
        return coursRepo.countCoursParFormateur();
    }

    public List<Object[]> etudiantsParGroupe() {
        return groupeRepo.countEtudiantsParGroupe();
    }

    public List<Object[]> coursParSpecialite() {
        return specialiteRepo.countCoursParSpecialite();
    }
}

