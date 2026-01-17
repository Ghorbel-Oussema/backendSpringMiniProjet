package com.example.MiniProjetBackend.controller.rest;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.MiniProjetBackend.entity.Etudiant;
import com.example.MiniProjetBackend.repository.GroupeRepository;
import com.example.MiniProjetBackend.service.EtudiantService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/etudiants")
public class EtudiantAdminRestController {

    private final EtudiantService etudiantService;
    private final GroupeRepository groupeRepo;

    public EtudiantAdminRestController(EtudiantService etudiantService, GroupeRepository groupeRepo) {
        this.etudiantService = etudiantService;
        this.groupeRepo = groupeRepo;
    }

    // ✅ LIST + SEARCH + PAGINATION
    // GET /api/admin/etudiants?keyword=&page=0&size=5
    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<Etudiant> etudiants = etudiantService.search(keyword, PageRequest.of(page, size));

        // Option 1: renvoyer juste la page
        // return ResponseEntity.ok(etudiants);

        // Option 2 (comme ton SSR): renvoyer aussi la liste des groupes
        Map<String, Object> res = new HashMap<>();
        res.put("etudiants", etudiants);
        res.put("keyword", keyword);
        res.put("groupes", groupeRepo.findAll());
        return ResponseEntity.ok(res);
    }

    // ✅ GET ONE (ex /edit/{matricule} SSR) -> REST standard
    // GET /api/admin/etudiants/{matricule}
    @GetMapping("/{matricule}")
    public ResponseEntity<Etudiant> getOne(@PathVariable String matricule) {
        Etudiant e = etudiantService.findByMatricule(matricule);
        return (e == null) ? ResponseEntity.notFound().build()
                           : ResponseEntity.ok(e);
    }

    // ✅ CREATE
    // POST /api/admin/etudiants  (JSON body)
    @PostMapping
    public ResponseEntity<Etudiant> create(@RequestBody Etudiant etudiant) {
        Etudiant saved = etudiantService.save(etudiant);
        return ResponseEntity.ok(saved);
    }

    // ✅ UPDATE (nom/prenom + affectation groupe)
    // PUT /api/admin/etudiants/{matricule}
    @PutMapping("/{matricule}")
    public ResponseEntity<Etudiant> update(
            @PathVariable String matricule,
            @RequestBody EtudiantUpdateRequest req
    ) {
        Etudiant e = etudiantService.findByMatricule(matricule);
        if (e == null) return ResponseEntity.notFound().build();

        e.setNom(req.getNom());
        e.setPrenom(req.getPrenom());
        etudiantService.save(e);

        // ✅ affectation (ou désaffectation)
        etudiantService.affecterGroupe(matricule, req.getGroupeId());

        // re-fetch optionnel si besoin d'avoir le groupe chargé
        Etudiant updated = etudiantService.findByMatricule(matricule);
        return ResponseEntity.ok(updated);
    }

    // ✅ DELETE
    // DELETE /api/admin/etudiants/{matricule}
    @DeleteMapping("/{matricule}")
    public ResponseEntity<Void> delete(@PathVariable String matricule) {
        etudiantService.delete(matricule);
        return ResponseEntity.noContent().build();
    }

    // ✅ Optionnel: endpoint pour récupérer les groupes (si Angular en a besoin séparément)
    // GET /api/admin/etudiants/groupes
    @GetMapping("/groupes")
    public ResponseEntity<?> groupes() {
        return ResponseEntity.ok(groupeRepo.findAll());
    }

    // DTO Request pour éviter d'exposer tout Etudiant en update
    public static class EtudiantUpdateRequest {
        private String nom;
        private String prenom;
        private Long groupeId;

        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }

        public String getPrenom() { return prenom; }
        public void setPrenom(String prenom) { this.prenom = prenom; }

        public Long getGroupeId() { return groupeId; }
        public void setGroupeId(Long groupeId) { this.groupeId = groupeId; }
    }
}
