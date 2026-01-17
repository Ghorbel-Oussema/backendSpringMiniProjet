package com.example.MiniProjetBackend.controller.rest;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.MiniProjetBackend.entity.Formateur;
import com.example.MiniProjetBackend.service.FormateurService;
import com.example.MiniProjetBackend.service.SpecialiteService;

@RestController
@RequestMapping("/api/admin/formateurs")
public class FormateurAdminRestController {

    private final FormateurService service;
    private final SpecialiteService specialiteService;

    public FormateurAdminRestController(FormateurService service, SpecialiteService specialiteService) {
        this.service = service;
        this.specialiteService = specialiteService;
    }

    // ✅ LIST + SEARCH + PAGINATION
    // GET /api/admin/formateurs?keyword=&page=0&size=5
    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<Formateur> formateurs = service.search(keyword, PageRequest.of(page, size));

        // Option 1: retourner juste Page<Formateur>
        // return ResponseEntity.ok(formateurs);

        // Option 2 (comme SSR): retourner aussi specialites + keyword
        Map<String, Object> res = new HashMap<>();
        res.put("formateurs", formateurs);
        res.put("specialites", specialiteService.findAll());
        res.put("keyword", keyword);
        return ResponseEntity.ok(res);
    }

    // ✅ GET ONE (ex /edit/{id} SSR) -> REST standard
    // GET /api/admin/formateurs/{id}
    // (tu peux retourner DTO comme dans ton service)
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        Object dto = service.getDtoById(id);
        return (dto == null) ? ResponseEntity.notFound().build()
                             : ResponseEntity.ok(dto);
    }

    // ✅ CREATE
    // POST /api/admin/formateurs   (JSON body)
    @PostMapping
    public ResponseEntity<Formateur> create(@RequestBody Formateur f) {
        Formateur saved = service.save(f);
        return ResponseEntity.ok(saved);
    }

    // ✅ UPDATE
    // PUT /api/admin/formateurs/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Formateur> update(@PathVariable Long id, @RequestBody Formateur f) {
        f.setId(id);
        Formateur updated = service.save(f);
        return ResponseEntity.ok(updated);
    }

    // ✅ DELETE
    // DELETE /api/admin/formateurs/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Optionnel: exposer les spécialités séparément pour Angular
    // GET /api/admin/formateurs/specialites
    @GetMapping("/specialites")
    public ResponseEntity<?> specialites() {
        return ResponseEntity.ok(specialiteService.findAll());
    }
}

