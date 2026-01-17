package com.example.MiniProjetBackend.controller.rest;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.MiniProjetBackend.dto.CoursDetailsDto;
import com.example.MiniProjetBackend.dto.EtudiantMeDto;
import com.example.MiniProjetBackend.dto.NoteDto;
import com.example.MiniProjetBackend.dto.SeanceDto;
import com.example.MiniProjetBackend.service.EtudiantService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


@RestController
@RequestMapping("/api/etudiant")
@CrossOrigin("http://localhost:4200")
public class EtudiantRestController {

    private final EtudiantService service;

    public EtudiantRestController(EtudiantService service) {
        this.service = service;
    }

    // ✅ Profil étudiant connecté
    @GetMapping("/me")
    public ResponseEntity<EtudiantMeDto> me(Principal principal) {
        return ResponseEntity.ok(service.getMe(principal.getName()));
    }

    // ✅ Notes de l'étudiant connecté
    @GetMapping("/me/notes")
    public ResponseEntity<List<NoteDto>> myNotes(Principal principal) {
        return ResponseEntity.ok(service.getNotes(principal.getName()));
    }

    // ✅ Notes par cours (optionnel)
    @GetMapping("/me/notes/{coursCode}")
    public ResponseEntity<List<NoteDto>> myNotesByCours(
            Principal principal,
            @PathVariable String coursCode) {
        return ResponseEntity.ok(service.getNotesByCours(principal.getName(), coursCode));
    }

    // ✅ Planning (séances du groupe) + filtre date
    @GetMapping("/me/seances")
    public ResponseEntity<List<SeanceDto>> mySeances(
            Principal principal,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(service.getPlanning(principal.getName(), from, to));
    }
    @GetMapping("/me/cours/{code}")
    public ResponseEntity<CoursDetailsDto> coursDetails(
            Principal principal,
            @PathVariable String code
    ) {
        return ResponseEntity.ok(service.getCoursDetails(principal.getName(), code));
    }
    
    
    
    @GetMapping("/me/seances/pdf")
    public ResponseEntity<byte[]> planningPdf(
            Principal principal,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        byte[] pdf = service.generatePlanningPdf(principal.getName(), from, to);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=planning.pdf")
                .body(pdf);
    }

}
