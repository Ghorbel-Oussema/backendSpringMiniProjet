package com.example.MiniProjetBackend.controller.rest;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.MiniProjetBackend.entity.Salle;
import com.example.MiniProjetBackend.service.SalleService;

@RestController
@RequestMapping("/api/admin/salles")
public class SalleRestController {

    private final SalleService salleService;

    public SalleRestController(SalleService salleService) {
        this.salleService = salleService;
    }

    // ✅ LIST + SEARCH + PAGINATION
    // GET /api/admin/salles?page=0&size=5&keyword=...
    @GetMapping
    public ResponseEntity<Page<Salle>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keyword
    ) {
        Page<Salle> salles = salleService.getSallesPage(page, size, keyword);
        return ResponseEntity.ok(salles);
    }

    // ✅ GET ONE (ex /edit/{id} SSR) -> REST standard
    // GET /api/admin/salles/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Salle> getOne(@PathVariable Long id) {
        Salle salle = salleService.getSalleById(id);
        return (salle == null) ? ResponseEntity.notFound().build()
                               : ResponseEntity.ok(salle);
    }

    // ✅ CREATE
    // POST /api/admin/salles (JSON body)
    @PostMapping
    public ResponseEntity<Salle> create(@RequestBody Salle salle) {
        // id doit être null en create
        salle.setId(null);
        Salle saved = salleService.saveOrUpdate(salle);
        return ResponseEntity.ok(saved);
    }

    // ✅ UPDATE
    // PUT /api/admin/salles/{id} (JSON body)
    @PutMapping("/{id}")
    public ResponseEntity<Salle> update(@PathVariable Long id, @RequestBody Salle body) {
        Salle existing = salleService.getSalleById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        existing.setNom(body.getNom());
        existing.setCapacite(body.getCapacite());

        Salle updated = salleService.saveOrUpdate(existing);
        return ResponseEntity.ok(updated);
    }

    // ✅ DELETE
    // DELETE /api/admin/salles/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        salleService.deleteSalle(id);
        return ResponseEntity.noContent().build();
    }
}

