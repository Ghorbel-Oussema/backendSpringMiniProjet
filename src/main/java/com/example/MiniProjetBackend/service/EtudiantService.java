package com.example.MiniProjetBackend.service;


import org.springframework.stereotype.Service;

import com.example.MiniProjetBackend.dto.EtudiantMeDto;
import com.example.MiniProjetBackend.dto.NoteDto;
import com.example.MiniProjetBackend.dto.SeanceDto;
import com.example.MiniProjetBackend.entity.Etudiant;
import com.example.MiniProjetBackend.entity.Groupe;
import com.example.MiniProjetBackend.entity.Note;
import com.example.MiniProjetBackend.entity.Seance;
import com.example.MiniProjetBackend.repository.CoursRepository;
import com.example.MiniProjetBackend.repository.EtudiantRepository;
import com.example.MiniProjetBackend.repository.GroupeRepository;
import com.example.MiniProjetBackend.repository.NoteRepository;
import com.example.MiniProjetBackend.repository.SeanceRepository;
import com.example.MiniProjetBackend.dto.CoursDetailsDto;
import com.example.MiniProjetBackend.entity.Cours;
import com.example.MiniProjetBackend.entity.Formateur;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.io.ByteArrayOutputStream;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

@Service
public class EtudiantService {
    @Autowired
    private final EtudiantRepository repo;
    private final GroupeRepository groupeRepo;
    private final SeanceRepository seanceRepo;
    private final NoteRepository noteRepo;


    private final CoursRepository coursRepo;

    public EtudiantService(
            EtudiantRepository repo,
            GroupeRepository groupeRepo,
            SeanceRepository seanceRepo,
            NoteRepository noteRepo,
            CoursRepository coursRepo
    ) {
        this.repo = repo;
        this.groupeRepo = groupeRepo;
        this.seanceRepo = seanceRepo;
        this.noteRepo = noteRepo;
        this.coursRepo = coursRepo;
    }

    public List<Etudiant> findAll() {
        return repo.findAll();
    }

    public Page<Etudiant> search(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return repo.findAll(pageable);
        }
        return repo.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(
                keyword, keyword, pageable);
    }

    public Etudiant findByMatricule(String matricule) {
        return repo.findById(matricule).orElseThrow();
    }

    public Etudiant save(Etudiant e) {
        Etudiant old = repo.findById(e.getMatricule()).orElse(null);
        if (old != null) {
            old.setNom(e.getNom());
            old.setPrenom(e.getPrenom());
            return repo.save(old);
        }
        return repo.save(e);
    }


    public void delete(String matricule) {
        repo.deleteById(matricule);
    }
    public void affecterGroupe(String matricule, Long groupeId) {
        Etudiant e = repo.findById(matricule)
                .orElseThrow(() -> new RuntimeException("Etudiant introuvable"));

        if (groupeId == null) {
            e.setGroupe(null);              // désaffecter
        } else {
            Groupe g = groupeRepo.findById(groupeId)
                    .orElseThrow(() -> new RuntimeException("Groupe introuvable"));
            e.setGroupe(g);
        }

        repo.save(e);
    }
    //
    private Etudiant getEtudiantByEmailOrThrow(String email) {
        return repo.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Etudiant introuvable pour email=" + email));
    }

    public EtudiantMeDto getMe(String email) {
        Etudiant e = getEtudiantByEmailOrThrow(email);

        EtudiantMeDto dto = new EtudiantMeDto();
        dto.matricule = e.getMatricule();
        dto.nom = e.getNom();
        dto.prenom = e.getPrenom();
        dto.email = e.getUser() != null ? e.getUser().getEmail() : null;

        Groupe g = e.getGroupe();
        if (g != null) {
            dto.groupeId = g.getId();
            dto.groupeNom = g.getNom();
        }
        return dto;
    }

    public List<SeanceDto> getPlanning(String email, LocalDate from, LocalDate to) {
        Etudiant e = getEtudiantByEmailOrThrow(email);
        if (e.getGroupe() == null) return List.of();

        Long gid = e.getGroupe().getId();

        List<Seance> seances = (from != null && to != null)
                ? seanceRepo.findByGroupe_IdAndDateBetweenOrderByDateAscHeureDebutAsc(gid, from, to)
                : seanceRepo.findByGroupe_IdOrderByDateAscHeureDebutAsc(gid);

        return seances.stream().map(this::toSeanceDto).collect(Collectors.toList());
    }

    public List<NoteDto> getNotes(String email) {
        Etudiant e = getEtudiantByEmailOrThrow(email);

        return noteRepo.findByEtudiant_Matricule(e.getMatricule())
                .stream().map(this::toNoteDto).collect(Collectors.toList());
    }

    public List<NoteDto> getNotesByCours(String email, String coursCode) {
        Etudiant e = getEtudiantByEmailOrThrow(email);

        return noteRepo.findByEtudiant_MatriculeAndCours_Code(e.getMatricule(), coursCode)
                .stream().map(this::toNoteDto).collect(Collectors.toList());
    }

    private SeanceDto toSeanceDto(Seance s) {
        SeanceDto dto = new SeanceDto();
        dto.id = s.getId();
        dto.date = s.getDate();
        dto.heureDebut = s.getHeureDebut();
        dto.heureFin = s.getHeureFin();

        if (s.getSalle() != null) {
            dto.salleId = s.getSalle().getId();
            dto.salleNom = s.getSalle().getNom();
        }

        if (s.getCours() != null) {
            dto.coursCode = s.getCours().getCode();
            dto.coursTitre = s.getCours().getTitre();

            if (s.getCours().getFormateur() != null) {
                dto.formateurId = s.getCours().getFormateur().getId();
                dto.formateurNom = s.getCours().getFormateur().getNom();
            }
        }

        if (s.getGroupe() != null) {
            dto.groupeId = s.getGroupe().getId();
            dto.groupeNom = s.getGroupe().getNom();
        }

        return dto;
    }
    private NoteDto toNoteDto(Note n) {
        NoteDto dto = new NoteDto();
        dto.id = n.getId();
        dto.valeur = n.getValeur();

        if (n.getCours() != null) {
            dto.coursCode = n.getCours().getCode();
            dto.coursTitre = n.getCours().getTitre();
        }
        return dto;
    }
    public CoursDetailsDto getCoursDetails(String email, String coursCode) {
        // Optionnel (recommandé): vérifier que l'étudiant est connecté
        getEtudiantByEmailOrThrow(email);

        Cours c = coursRepo.findById(coursCode)
                .orElseThrow(() -> new RuntimeException("Cours introuvable: " + coursCode));

        CoursDetailsDto dto = new CoursDetailsDto();
        dto.code = c.getCode();
        dto.titre = c.getTitre();
        dto.description = c.getDescription();

        Formateur f = c.getFormateur();
        if (f != null) {
            dto.formateurId = f.getId();
            dto.formateurNom = f.getNom();

            if (f.getSpecialite() != null) {
                dto.specialiteId = f.getSpecialite().getId();
                dto.specialiteNom = f.getSpecialite().getNom();
            }
        }
        return dto;
    }
    public byte[] generatePlanningPdf(String email, LocalDate from, LocalDate to) {
        EtudiantMeDto me = getMe(email);
        List<SeanceDto> seances = getPlanning(email, from, to);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, out);

            doc.open();

            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 11);

            doc.add(new Paragraph("Planning Étudiant", titleFont));
            doc.add(new Paragraph("Matricule: " + me.matricule + " | Nom: " + me.nom + " " + me.prenom, normalFont));
            doc.add(new Paragraph("Groupe: " + (me.groupeNom != null ? me.groupeNom : "-"), normalFont));
            doc.add(new Paragraph("Période: " + (from != null ? from : "-") + " → " + (to != null ? to : "-"), normalFont));
            doc.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2.2f, 1.5f, 1.5f, 2.2f, 2.2f, 2.2f});

            addHeader(table, "Date");
            addHeader(table, "Début");
            addHeader(table, "Fin");
            addHeader(table, "Cours");
            addHeader(table, "Salle");
            addHeader(table, "Formateur");

            for (SeanceDto s : seances) {
                table.addCell(cell(String.valueOf(s.date)));
                table.addCell(cell(String.valueOf(s.heureDebut)));
                table.addCell(cell(String.valueOf(s.heureFin)));
                table.addCell(cell(s.coursTitre + " (" + s.coursCode + ")"));
                table.addCell(cell(s.salleNom != null ? s.salleNom : "-"));
                table.addCell(cell(s.formateurNom != null ? s.formateurNom : "-"));
            }

            doc.add(table);
            doc.close();

            return out.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("Erreur génération PDF planning", ex);
        }
    }

    private void addHeader(PdfPTable table, String text) {
        Font font = new Font(Font.HELVETICA, 11, Font.BOLD);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(6);
        table.addCell(cell);
    }

    private PdfPCell cell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text == null ? "-" : text));
        cell.setPadding(5);
        return cell;
    }
    
}
