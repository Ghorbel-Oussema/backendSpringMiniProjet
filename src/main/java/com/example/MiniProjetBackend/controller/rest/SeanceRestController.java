package com.example.MiniProjetBackend.controller.rest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.MiniProjetBackend.entity.Seance;
import com.example.MiniProjetBackend.service.CoursService;
import com.example.MiniProjetBackend.service.GroupeService;
import com.example.MiniProjetBackend.service.SalleService;
import com.example.MiniProjetBackend.service.SeanceService;

@RestController
@RequestMapping("/api/admin/seances")
public class SeanceRestController {

    private final SeanceService service;
    private final CoursService coursService;
    private final SalleService salleService;
    private final GroupeService groupeService;

    public SeanceRestController(SeanceService service,
                                CoursService coursService,
                                SalleService salleService,
                                GroupeService groupeService) {
        this.service = service;
        this.coursService = coursService;
        this.salleService = salleService;
        this.groupeService = groupeService;
    }

    // ✅ LIST + SEARCH + PAGINATION (+ data select: cours/salles/groupes)
    // GET /api/admin/seances?keyword=&page=0&size=5
    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<Seance> seances = service.search(keyword, PageRequest.of(page, size));

        Map<String, Object> res = new HashMap<>();
        res.put("seances", seances);
        res.put("keyword", keyword);
        res.put("cours", coursService.findAll());
        res.put("salles", salleService.findAll());
        res.put("groupes", groupeService.findAll());
        return ResponseEntity.ok(res);
    }

    // ✅ GET ONE
    // GET /api/admin/seances/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Seance> getOne(@PathVariable Long id) {
        Seance s = service.findById(id);
        return (s == null) ? ResponseEntity.notFound().build()
                           : ResponseEntity.ok(s);
    }

    // ✅ CREATE
    // POST /api/admin/seances
    @PostMapping
    public ResponseEntity<Seance> create(@RequestBody SeanceUpsertRequest req) {
        Seance s = new Seance();
        apply(req, s);
        service.save(s);
        return ResponseEntity.ok(s);
    }

    // ✅ UPDATE
    // PUT /api/admin/seances/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Seance> update(@PathVariable Long id,
                                        @RequestBody SeanceUpsertRequest req) {

        Seance s = service.findById(id);
        if (s == null) return ResponseEntity.notFound().build();

        apply(req, s);
        service.save(s);
        return ResponseEntity.ok(s);
    }

    // ✅ DELETE
    // DELETE /api/admin/seances/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ OPTIONNEL: pour remplir les dropdowns dans Angular
    // GET /api/admin/seances/form-data
    @GetMapping("/form-data")
    public ResponseEntity<?> formData() {
        Map<String, Object> res = new HashMap<>();
        res.put("cours", coursService.findAll());
        res.put("salles", salleService.findAll());
        res.put("groupes", groupeService.findAll());
        return ResponseEntity.ok(res);
    }

    // ---- Helpers ----

    private void apply(SeanceUpsertRequest req, Seance s) {
        s.setDate(LocalDate.parse(req.getDate()));
        s.setHeureDebut(LocalTime.parse(req.getHeureDebut()));
        s.setHeureFin(LocalTime.parse(req.getHeureFin()));

        if (req.getCoursCode() != null && !req.getCoursCode().isBlank())
            s.setCours(coursService.findById(req.getCoursCode()));
        else
            s.setCours(null);

        if (req.getSalleId() != null)
            s.setSalle(salleService.getSalleById(req.getSalleId()));
        else
            s.setSalle(null);

        if (req.getGroupeId() != null)
            s.setGroupe(groupeService.findById(req.getGroupeId()));
        else
            s.setGroupe(null);
    }
 // ✅ GET ALL (détails complets: cours + salle + groupe)
 // GET /api/admin/seances/all
 @GetMapping("/all")
 public ResponseEntity<?> allWithDetails() {
     return ResponseEntity.ok(service.findAllWithDetails());
 }


    // DTO request (clean pour Angular)
    public static class SeanceUpsertRequest {
        private String date;        // "2026-01-17"
        private String heureDebut;  // "08:30"
        private String heureFin;    // "10:00"
        private String coursCode;   // ex: "CS101"
        private Long salleId;       // ex: 3
        private Long groupeId;      // ex: 2

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }

        public String getHeureDebut() { return heureDebut; }
        public void setHeureDebut(String heureDebut) { this.heureDebut = heureDebut; }

        public String getHeureFin() { return heureFin; }
        public void setHeureFin(String heureFin) { this.heureFin = heureFin; }

        public String getCoursCode() { return coursCode; }
        public void setCoursCode(String coursCode) { this.coursCode = coursCode; }

        public Long getSalleId() { return salleId; }
        public void setSalleId(Long salleId) { this.salleId = salleId; }

        public Long getGroupeId() { return groupeId; }
        public void setGroupeId(Long groupeId) { this.groupeId = groupeId; }
    }
}
