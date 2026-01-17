package com.example.MiniProjetBackend.controller.rest;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.MiniProjetBackend.entity.Groupe;
import com.example.MiniProjetBackend.service.GroupeService;

@RestController
@RequestMapping("/api/admin/groupes")
public class GroupeRestController {

    private final GroupeService service;

    public GroupeRestController(GroupeService service) {
        this.service = service;
    }

    // ✅ LIST + SEARCH + PAGINATION
    // GET /api/admin/groupes?keyword=&page=0&size=5
    @GetMapping
    public ResponseEntity<Page<Groupe>> list(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<Groupe> groupes = service.search(keyword, PageRequest.of(page, size));
        return ResponseEntity.ok(groupes);
    }

    // ✅ GET ONE (ex /edit/{id} SSR) -> REST standard
    // GET /api/admin/groupes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Groupe> getOne(@PathVariable Long id) {
        Groupe g = service.findById(id);
        return (g == null) ? ResponseEntity.notFound().build()
                           : ResponseEntity.ok(g);
    }

    // ✅ CREATE
    // POST /api/admin/groupes  (JSON body)
    @PostMapping
    public ResponseEntity<Groupe> create(@RequestBody Groupe g) {
        Groupe saved = service.savee(g);
        return ResponseEntity.ok(saved);
    }

    // ✅ UPDATE
    // PUT /api/admin/groupes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Groupe> update(@PathVariable Long id, @RequestBody Groupe g) {
        g.setId(id);
        Groupe updated = service.savee(g);
        return ResponseEntity.ok(updated);
    }

    // ✅ DELETE
    // DELETE /api/admin/groupes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

