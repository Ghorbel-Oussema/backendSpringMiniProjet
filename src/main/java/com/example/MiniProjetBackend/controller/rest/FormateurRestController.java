package com.example.MiniProjetBackend.controller.rest;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.MiniProjetBackend.dto.CoursDetailsDto;
import com.example.MiniProjetBackend.dto.CoursDto;
import com.example.MiniProjetBackend.dto.EtudiantMeDto;
import com.example.MiniProjetBackend.dto.FormateurDto;
import com.example.MiniProjetBackend.dto.GroupeMiniDto;
import com.example.MiniProjetBackend.dto.GroupeNotesDto;
import com.example.MiniProjetBackend.dto.NoteDto;
import com.example.MiniProjetBackend.dto.NoteUpsertDto;
import com.example.MiniProjetBackend.dto.SeanceDto;
import com.example.MiniProjetBackend.service.FormateurService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
@RestController
@RequestMapping("/api/formateur")
@CrossOrigin("http://localhost:4200")
public class FormateurRestController {

  private final FormateurService service;

  public FormateurRestController(FormateurService service) {
    this.service = service;
  }

  // ===== PROFIL =====
  @GetMapping("/me")
  public ResponseEntity<FormateurDto> me(Principal principal) {
    return ResponseEntity.ok(service.getMe(principal.getName()));
  }

  // ===== COURS =====
  @GetMapping("/me/cours")
  public ResponseEntity<List<CoursDto>> myCours(Principal principal) {
    return ResponseEntity.ok(service.myCours(principal.getName()));
  }

  @GetMapping("/me/cours/{code}")
  public ResponseEntity<CoursDetailsDto> coursDetails(
      Principal principal, @PathVariable String code) {
    return ResponseEntity.ok(service.coursDetails(principal.getName(), code));
  }

  // ✅ Séances d'un cours
  @GetMapping("/me/cours/{code}/seances")
  public ResponseEntity<List<SeanceDto>> coursSeances(
      Principal principal,
      @PathVariable String code,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
  ) {
    return ResponseEntity.ok(service.getCoursSeances(principal.getName(), code, from, to));
  }

  // ✅ Groupes concernés par un cours (distinct)
  @GetMapping("/me/cours/{code}/groupes")
  public ResponseEntity<List<GroupeMiniDto>> coursGroupes(
      Principal principal, @PathVariable String code) {
    return ResponseEntity.ok(service.getGroupesByCours(principal.getName(), code));
  }

  // ===== PLANNING GLOBAL + FILTRES =====
  @GetMapping("/me/seances")
  public ResponseEntity<List<SeanceDto>> myPlanning(
      Principal principal,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
      @RequestParam(required = false) String coursCode,
      @RequestParam(required = false) Long groupeId,
      @RequestParam(required = false) Long salleId
  ) {
    return ResponseEntity.ok(service.myPlanningFiltered(
        principal.getName(), from, to, coursCode, groupeId, salleId
    ));
  }

  // ===== NOTES =====
  @GetMapping("/groupes/{groupeId}/etudiants")
  public ResponseEntity<List<EtudiantMeDto>> students(@PathVariable Long groupeId) {
    return ResponseEntity.ok(service.studentsOfGroup(groupeId));
  }

  // ✅ récupérer notes existantes pour pré-remplissage
  @GetMapping("/me/notes")
  public ResponseEntity<List<NoteDto>> notesByCoursAndGroupe(
      Principal principal,
      @RequestParam String coursCode,
      @RequestParam Long groupeId
  ) {
    return ResponseEntity.ok(service.getNotesCoursGroupe(principal.getName(), coursCode, groupeId));
  }

  @PostMapping("/notes")
  public ResponseEntity<NoteDto> upsertNote(Principal principal, @RequestBody NoteUpsertDto req) {
    return ResponseEntity.ok(service.upsertNote(principal.getName(), req));
  }

  // ===== STATS (optionnel) =====
  @GetMapping("/stats/cours/{code}/moyenne")
  public ResponseEntity<Double> avgCours(Principal principal, @PathVariable String code) {
    return ResponseEntity.ok(service.avgCours(principal.getName(), code));
  }

  @GetMapping("/stats/cours/{code}/groupes/{groupeId}/moyenne")
  public ResponseEntity<Double> avgCoursGroupe(
      Principal principal, @PathVariable String code, @PathVariable Long groupeId) {
    return ResponseEntity.ok(service.avgCoursGroupe(principal.getName(), code, groupeId));
  }
  
  @GetMapping("/me/notes/pdf")
  public ResponseEntity<byte[]> notesPdf(
      Principal principal,
      @RequestParam String coursCode,
      @RequestParam Long groupeId
  ) {
    byte[] pdf = service.generateNotesPdf(principal.getName(), coursCode, groupeId);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_PDF)
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=notes_" + coursCode + "_groupe_" + groupeId + ".pdf")
        .body(pdf);
  }
  @GetMapping("/me/cours/{code}/groupes-notes")
  public ResponseEntity<List<GroupeNotesDto>> groupesAvecNotes(
      Principal principal,
      @PathVariable String code
  ) {
    return ResponseEntity.ok(service.groupesAvecNotes(principal.getName(), code));
  }
  @GetMapping("/me/cours/{code}/groupes/{groupeId}/etudiants")
  public ResponseEntity<List<EtudiantMeDto>> studentsOfGroupForCours(
      Principal principal,
      @PathVariable String code,
      @PathVariable Long groupeId
  ) {
    return ResponseEntity.ok(service.studentsOfGroupForCours(principal.getName(), code, groupeId));
  }


}
