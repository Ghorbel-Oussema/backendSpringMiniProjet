package com.example.MiniProjetBackend.service;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.MiniProjetBackend.dto.CoursDetailsDto;
import com.example.MiniProjetBackend.dto.CoursDto;
import com.example.MiniProjetBackend.dto.EtudiantMeDto;
import com.example.MiniProjetBackend.dto.FormateurDto;
import com.example.MiniProjetBackend.dto.GroupeMiniDto;
import com.example.MiniProjetBackend.dto.GroupeNotesDto;
import com.example.MiniProjetBackend.dto.NoteDto;
import com.example.MiniProjetBackend.dto.NoteUpsertDto;
import com.example.MiniProjetBackend.dto.SeanceDto;
import com.example.MiniProjetBackend.entity.Cours;
import com.example.MiniProjetBackend.entity.Etudiant;
import com.example.MiniProjetBackend.entity.Formateur;
import com.example.MiniProjetBackend.entity.Note;
import com.example.MiniProjetBackend.entity.Seance;
import com.example.MiniProjetBackend.repository.CoursRepository;
import com.example.MiniProjetBackend.repository.EtudiantRepository;
import com.example.MiniProjetBackend.repository.FormateurRepository;
import com.example.MiniProjetBackend.repository.NoteRepository;
import com.example.MiniProjetBackend.repository.SeanceRepository;
import java.io.ByteArrayOutputStream;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

@Service
public class FormateurService {

    private final FormateurRepository formateurRepository;
    private final CoursRepository coursRepo;
    private final SeanceRepository seanceRepo;
    private final EtudiantRepository etudiantRepo;
    private final NoteRepository noteRepo;

    public FormateurService(
    	      FormateurRepository formateurRepository,
    	      CoursRepository coursRepo,
    	      SeanceRepository seanceRepo,
    	      EtudiantRepository etudiantRepo,
    	      NoteRepository noteRepo
    	  ) {
    	    this.formateurRepository = formateurRepository;
    	    this.coursRepo = coursRepo;
    	    this.seanceRepo = seanceRepo;
    	    this.etudiantRepo = etudiantRepo;
    	    this.noteRepo = noteRepo;
    	  }
    public List<Formateur> findAll() {
        return formateurRepository.findAll();
    }

    public Page<Formateur> search(String keyword, Pageable pageable) {
        return formateurRepository.findByNomContainingIgnoreCase(keyword, pageable);
    }

    public Formateur save(Formateur formateur) {
        return formateurRepository.save(formateur);
    }

    public Formateur findById(Long id) {
        return formateurRepository.findById(id).orElseThrow();

    }
    public void delete(Long id) {
    	formateurRepository.deleteById(id);
    }
    
    public FormateurDto getDtoById(Long id) {

        Formateur f = formateurRepository
                .findByIdWithUserAndSpecialite(id)
                .orElseThrow(() -> new RuntimeException("Formateur introuvable"));

        FormateurDto dto = new FormateurDto();
        dto.setId(f.getId());
        dto.setNom(f.getNom());

        if (f.getUser() != null) {
            dto.setEmail(f.getUser().getEmail());
        }

        if (f.getSpecialite() != null) {
            dto.setSpecialiteId(f.getSpecialite().getId());
            dto.setSpecialiteNom(f.getSpecialite().getNom());
        }

        return dto;
    }
    
    
    private Formateur getFormateurOrThrow(String email) {
        return formateurRepository.findByUser_Email(email)
          .orElseThrow(() -> new RuntimeException("Formateur introuvable"));
      }

      public FormateurDto getMe(String email) {
        Formateur f = getFormateurOrThrow(email);
        FormateurDto dto = new FormateurDto();
        dto.id = f.getId();
        dto.nom = f.getNom();
        dto.email = f.getUser() != null ? f.getUser().getEmail() : null;
        if (f.getSpecialite() != null) {
          dto.specialiteId = f.getSpecialite().getId();
          dto.specialiteNom = f.getSpecialite().getNom();
        }
        return dto;
      }

      public List<CoursDto> myCours(String email) {
        Formateur f = getFormateurOrThrow(email);
        return coursRepo.findByFormateur_Id(f.getId()).stream().map(c -> {
          CoursDto dto = new CoursDto();
          dto.code = c.getCode();
          dto.titre = c.getTitre();
          dto.description = c.getDescription();
          return dto;
        }).toList();
      }

      public CoursDetailsDto coursDetails(String email, String code) {
        Formateur f = getFormateurOrThrow(email);
        Cours c = coursRepo.findByCodeAndFormateur_Id(code, f.getId())
          .orElseThrow(() -> new RuntimeException("Cours introuvable ou non autorisé"));

        CoursDetailsDto dto = new CoursDetailsDto();
        dto.code = c.getCode();
        dto.titre = c.getTitre();
        dto.description = c.getDescription();

        dto.formateurId = f.getId();
        dto.formateurNom = f.getNom();

        if (f.getSpecialite() != null) {
          dto.specialiteId = f.getSpecialite().getId();
          dto.specialiteNom = f.getSpecialite().getNom();
        }
        return dto;
      }

      public List<SeanceDto> myPlanning(String email, LocalDate from, LocalDate to) {
        Formateur f = getFormateurOrThrow(email);

        List<Seance> seances = (from != null && to != null)
          ? seanceRepo.findByCours_Formateur_IdAndDateBetweenOrderByDateAscHeureDebutAsc(f.getId(), from, to)
          : seanceRepo.findByCours_Formateur_IdOrderByDateAscHeureDebutAsc(f.getId());

        // réutilise ton mapper toSeanceDto (comme étudiant)
        return seances.stream().map(this::toSeanceDto).toList();
      }

      // ✅ Liste étudiants d’un groupe (pour saisir notes)
      public List<EtudiantMeDto> studentsOfGroup(Long groupeId) {
        return etudiantRepo.findByGroupe_IdOrderByNomAscPrenomAsc(groupeId).stream().map(e -> {
        	EtudiantMeDto dto = new EtudiantMeDto();
          dto.matricule = e.getMatricule();
          dto.nom = e.getNom();
          dto.prenom = e.getPrenom();
          if (e.getGroupe() != null) {
            dto.groupeId = e.getGroupe().getId();
            dto.groupeNom = e.getGroupe().getNom();
          }
          return dto;
        }).toList();
      }

      // ✅ Upsert note (create si pas existe / update sinon)
      public NoteDto upsertNote(String email, NoteUpsertDto req) {
        Formateur f = getFormateurOrThrow(email);

        // sécurité : le cours doit appartenir au formateur
        coursRepo.findByCodeAndFormateur_Id(req.coursCode, f.getId())
          .orElseThrow(() -> new RuntimeException("Cours non autorisé"));

        Note note = noteRepo.findByEtudiant_MatriculeAndCours_Code(req.etudiantMatricule, req.coursCode)
          .orElseGet(Note::new);

        // si new => associer
        if (note.getId() == null) {
          Etudiant e = etudiantRepo.findById(req.etudiantMatricule).orElseThrow();
          Cours c = coursRepo.findById(req.coursCode).orElseThrow();
          note.setEtudiant(e);
          note.setCours(c);
        }

        note.setValeur(req.valeur);
        Note saved = noteRepo.save(note);

        NoteDto dto = new NoteDto();
        dto.id = saved.getId();
        dto.valeur = saved.getValeur();
        dto.coursCode = saved.getCours().getCode();
        dto.coursTitre = saved.getCours().getTitre();
        return dto;
      }

      private SeanceDto toSeanceDto(Seance s) {
        SeanceDto dto = new SeanceDto();
        dto.id = s.getId();
        dto.date = s.getDate();
        dto.heureDebut = s.getHeureDebut();
        dto.heureFin = s.getHeureFin();
        if (s.getSalle() != null) { dto.salleId = s.getSalle().getId(); dto.salleNom = s.getSalle().getNom(); }
        if (s.getGroupe() != null) { dto.groupeId = s.getGroupe().getId(); dto.groupeNom = s.getGroupe().getNom(); }
        if (s.getCours() != null) { dto.coursCode = s.getCours().getCode(); dto.coursTitre = s.getCours().getTitre(); }
        return dto;
      }
      public List<SeanceDto> getCoursSeances(String email, String code, LocalDate from, LocalDate to) {
    	  Formateur f = getFormateurOrThrow(email);

    	  // sécurité: cours appartient au formateur
    	  coursRepo.findByCodeAndFormateur_Id(code, f.getId())
    	    .orElseThrow(() -> new RuntimeException("Cours non autorisé"));

    	  List<Seance> seances = (from != null && to != null)
    	    ? seanceRepo.findByCours_CodeAndCours_Formateur_IdAndDateBetweenOrderByDateAscHeureDebutAsc(code, f.getId(), from, to)
    	    : seanceRepo.findByCours_CodeAndCours_Formateur_IdOrderByDateAscHeureDebutAsc(code, f.getId());

    	  return seances.stream().map(this::toSeanceDto).toList();
    	}
      public List<GroupeMiniDto> getGroupesByCours(String email, String code) {
    	  Formateur f = getFormateurOrThrow(email);

    	  coursRepo.findByCodeAndFormateur_Id(code, f.getId())
    	    .orElseThrow(() -> new RuntimeException("Cours non autorisé"));

    	  return seanceRepo.findByCours_CodeAndCours_Formateur_IdOrderByDateAscHeureDebutAsc(code, f.getId())
    	    .stream()
    	    .filter(s -> s.getGroupe() != null)
    	    .map(s -> s.getGroupe())
    	    .distinct()
    	    .map(g -> {
    	      GroupeMiniDto dto = new GroupeMiniDto();
    	      dto.id = g.getId();
    	      dto.nom = g.getNom();
    	      return dto;
    	    })
    	    .toList();
    	}
      public List<NoteDto> getNotesCoursGroupe(String email, String coursCode, Long groupeId) {
    	  Formateur f = getFormateurOrThrow(email);

    	  coursRepo.findByCodeAndFormateur_Id(coursCode, f.getId())
    	    .orElseThrow(() -> new RuntimeException("Cours non autorisé"));

    	  return noteRepo.findByCours_CodeAndEtudiant_Groupe_Id(coursCode, groupeId)
    	    .stream()
    	    .map(this::toNoteDto)
    	    .toList();
    	}

    	private NoteDto toNoteDto(Note n) {
    	  NoteDto dto = new NoteDto();
    	  dto.id = n.getId();
    	  dto.valeur = n.getValeur();
    	  if (n.getEtudiant() != null) {
    		    dto.etudiantMatricule = n.getEtudiant().getMatricule();   // ✅ IMPORTANT
    		  }
    	  if (n.getCours() != null) {
    	    dto.coursCode = n.getCours().getCode();
    	    dto.coursTitre = n.getCours().getTitre();
    	  }
    	  return dto;
    	}
    	public List<SeanceDto> myPlanningFiltered(
    		    String email, LocalDate from, LocalDate to,
    		    String coursCode, Long groupeId, Long salleId
    		) {
    		  Formateur f = getFormateurOrThrow(email);

    		  // tu peux faire simple: récupérer tout puis filtrer en Java
    		  List<Seance> base = (from != null && to != null)
    		    ? seanceRepo.findByCours_Formateur_IdAndDateBetweenOrderByDateAscHeureDebutAsc(f.getId(), from, to)
    		    : seanceRepo.findByCours_Formateur_IdOrderByDateAscHeureDebutAsc(f.getId());

    		  return base.stream()
    		    .filter(s -> coursCode == null || (s.getCours() != null && coursCode.equals(s.getCours().getCode())))
    		    .filter(s -> groupeId == null || (s.getGroupe() != null && groupeId.equals(s.getGroupe().getId())))
    		    .filter(s -> salleId == null || (s.getSalle() != null && salleId.equals(s.getSalle().getId())))
    		    .map(this::toSeanceDto)
    		    .toList();
    		}
    	public Double avgCours(String email, String code) {
    		  Formateur f = getFormateurOrThrow(email);

    		  // sécurité : cours appartient au formateur
    		  coursRepo.findByCodeAndFormateur_Id(code, f.getId())
    		    .orElseThrow(() -> new RuntimeException("Cours non autorisé"));

    		  Double avg = noteRepo.avgByCours(code);
    		  return avg == null ? 0.0 : avg;
    		}

    		public Double avgCoursGroupe(String email, String code, Long groupeId) {
    		  Formateur f = getFormateurOrThrow(email);

    		  // sécurité : cours appartient au formateur
    		  coursRepo.findByCodeAndFormateur_Id(code, f.getId())
    		    .orElseThrow(() -> new RuntimeException("Cours non autorisé"));

    		  Double avg = noteRepo.avgByCoursAndGroupe(code, groupeId);
    		  return avg == null ? 0.0 : avg;
    		}

    		public byte[] generateNotesPdf(String email, String coursCode, Long groupeId) {
    			  Formateur f = getFormateurOrThrow(email);

    			  // sécurité : cours appartient au formateur
    			  Cours c = coursRepo.findByCodeAndFormateur_Id(coursCode, f.getId())
    			      .orElseThrow(() -> new RuntimeException("Cours non autorisé"));

    			  // étudiants du groupe
    			  List<Etudiant> etudiants = etudiantRepo.findByGroupe_IdOrderByNomAscPrenomAsc(groupeId);

    			  // notes existantes pour ce cours+groupe
    			  List<Note> notes = noteRepo.findByCours_CodeAndEtudiant_Groupe_Id(coursCode, groupeId);

    			  // map matricule -> note
    			  java.util.Map<String, Double> noteMap = notes.stream()
    			      .collect(java.util.stream.Collectors.toMap(
    			          n -> n.getEtudiant().getMatricule(),
    			          n -> n.getValeur(),
    			          (a, b) -> a
    			      ));

    			  ByteArrayOutputStream out = new ByteArrayOutputStream();
    			  Document doc = new Document(PageSize.A4);
    			  PdfWriter.getInstance(doc, out);

    			  doc.open();

    			  doc.add(new Paragraph("Notes - " + c.getTitre() + " (" + c.getCode() + ")"));
    			  doc.add(new Paragraph("Formateur: " + f.getNom()));
    			  doc.add(new Paragraph("Groupe ID: " + groupeId));
    			  doc.add(new Paragraph(" "));

    			  PdfPTable table = new PdfPTable(4);
    			  table.setWidthPercentage(100);
    			  table.setWidths(new float[]{3, 4, 4, 2});

    			  table.addCell("Matricule");
    			  table.addCell("Nom");
    			  table.addCell("Prénom");
    			  table.addCell("Note");

    			  for (Etudiant e : etudiants) {
    			    table.addCell(e.getMatricule());
    			    table.addCell(e.getNom() == null ? "" : e.getNom());
    			    table.addCell(e.getPrenom() == null ? "" : e.getPrenom());
    			    Double v = noteMap.get(e.getMatricule());
    			    table.addCell(v == null ? "-" : String.valueOf(v));
    			  }

    			  doc.add(table);
    			  doc.close();

    			  return out.toByteArray();
    			}
    		public List<GroupeNotesDto> groupesAvecNotes(String email, String code) {
    			  Formateur f = getFormateurOrThrow(email);

    			  coursRepo.findByCodeAndFormateur_Id(code, f.getId())
    			       .orElseThrow(() -> new RuntimeException("Cours non autorisé"));

    			  // groupes concernés
    			  List<GroupeMiniDto> groupes = getGroupesByCours(email, code);

    			  return groupes.stream().map(g -> {
    			    GroupeNotesDto dto = new GroupeNotesDto();
    			    dto.groupeId = g.id;
    			    dto.groupeNom = g.nom;

    			    // moyenne du groupe pour ce cours
    			    Double avg = noteRepo.avgByCoursAndGroupe(code, g.id);
    			    dto.moyenne = (avg == null) ? 0.0 : avg;
    			    return dto;
    			  }).toList();
    			}
    		public List<EtudiantMeDto> studentsOfGroupForCours(String email, String code, Long groupeId) {
    			  Formateur f = getFormateurOrThrow(email);

    			  coursRepo.findByCodeAndFormateur_Id(code, f.getId())
    			    .orElseThrow(() -> new RuntimeException("Cours non autorisé"));

    			  boolean ok = seanceRepo.existsByCours_CodeAndGroupe_Id(code, groupeId);
    			  if (!ok) throw new RuntimeException("Ce groupe ne suit pas ce cours");

    			  return studentsOfGroup(groupeId);
    			}






}
