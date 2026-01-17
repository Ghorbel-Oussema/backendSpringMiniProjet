package com.example.MiniProjetBackend.controller.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.MiniProjetBackend.entity.Note;
import com.example.MiniProjetBackend.service.CoursService;
import com.example.MiniProjetBackend.service.EtudiantService;
import com.example.MiniProjetBackend.service.NoteService;

@RestController
@RequestMapping("/api/admin/notes")
public class NoteRestController {

    private final NoteService noteService;
    private final EtudiantService etudiantService;
    private final CoursService coursService;

    public NoteRestController(NoteService noteService,
                              EtudiantService etudiantService,
                              CoursService coursService) {
        this.noteService = noteService;
        this.etudiantService = etudiantService;
        this.coursService = coursService;
    }

    // ✅ LIST + PAGINATION
    // GET /api/admin/notes?page=0&size=5
    @GetMapping
    public ResponseEntity<Page<Note>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<Note> notes = noteService.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok(notes);
    }

    // ✅ GET ONE (ex /edit/{id})
    // GET /api/admin/notes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Note> getOne(@PathVariable Long id) {
        Note note = noteService.findById(id);
        return (note == null) ? ResponseEntity.notFound().build()
                              : ResponseEntity.ok(note);
    }

    // ✅ CREATE
    // POST /api/admin/notes   (JSON body)
    @PostMapping
    public ResponseEntity<Note> create(@RequestBody Note note) {
        Note saved = noteService.savee(note);
        return ResponseEntity.ok(saved);
    }

    // ✅ UPDATE
    // PUT /api/admin/notes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Note> update(@PathVariable Long id,
                                       @RequestBody Note note) {
        note.setId(id);
        Note updated = noteService.savee(note);
        return ResponseEntity.ok(updated);
    }

    // ✅ DELETE
    // DELETE /api/admin/notes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        noteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ OPTIONNEL : données nécessaires au formulaire Angular
    // GET /api/admin/notes/form-data
    @GetMapping("/form-data")
    public ResponseEntity<?> formData() {
        Map<String, Object> res = new HashMap<>();
        res.put("etudiants", etudiantService.findAll());
        res.put("cours", coursService.findAll());
        return ResponseEntity.ok(res);
    }
}

