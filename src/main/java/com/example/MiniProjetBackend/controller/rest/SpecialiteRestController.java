package com.example.MiniProjetBackend.controller.rest;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.MiniProjetBackend.entity.Specialite;
import com.example.MiniProjetBackend.service.SpecialiteService;

@RestController
@RequestMapping("/api/admin/specialites")
public class SpecialiteRestController {

    private final SpecialiteService service;

    public SpecialiteRestController(SpecialiteService service) {
        this.service = service;
    }

    // ✅ LIST + SEARCH + PAGINATION
    // GET /api/admin/specialites?keyword=&page=0&size=5
    @GetMapping
    public ResponseEntity<Page<Specialite>> list(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<Specialite> specialites = service.search(keyword, PageRequest.of(page, size));
        return ResponseEntity.ok(specialites);
    }

    // ✅ GET ONE (ex /edit/{id} SSR) -> REST standard
    // GET /api/admin/specialites/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Specialite> getOne(@PathVariable Long id) {
        Specialite s = service.findById(id);
        return (s == null) ? ResponseEntity.notFound().build()
                           : ResponseEntity.ok(s);
    }

    // ✅ CREATE
    // POST /api/admin/specialites (JSON body)
    @PostMapping
    public ResponseEntity<Specialite> create(@RequestBody Specialite s) {
        s.setId(null); // create
        Specialite saved = service.save(s);
        return ResponseEntity.ok(saved);
    }

    // ✅ UPDATE
    // PUT /api/admin/specialites/{id} (JSON body)
    @PutMapping("/{id}")
    public ResponseEntity<Specialite> update(@PathVariable Long id, @RequestBody Specialite s) {
        s.setId(id);
        Specialite updated = service.save(s);
        return ResponseEntity.ok(updated);
    }

    // ✅ DELETE
    // DELETE /api/admin/specialites/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
