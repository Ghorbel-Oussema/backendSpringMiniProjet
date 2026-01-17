package com.example.MiniProjetBackend.controller.rest;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.MiniProjetBackend.entity.Cours;
import com.example.MiniProjetBackend.service.CoursService;

@RestController
@RequestMapping("/api/admin/cours")
public class CoursRestController {

    private final CoursService coursService;

    public CoursRestController(CoursService coursService) {
        this.coursService = coursService;
    }

    @GetMapping
    public ResponseEntity<Page<Cours>> list(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(coursService.search(keyword, PageRequest.of(page, size)));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getOne(@PathVariable String code) {
        return ResponseEntity.ok(coursService.findByCode(code));
    }

    @PostMapping
    public ResponseEntity<Cours> create(@RequestBody Cours cours) {
        return ResponseEntity.ok(coursService.save(cours));
    }

    @PutMapping("/{code}")
    public ResponseEntity<Cours> update(@PathVariable String code, @RequestBody Cours cours) {
        cours.setCode(code);
        return ResponseEntity.ok(coursService.save(cours));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        coursService.delete(code);
        return ResponseEntity.noContent().build();
    }
}
