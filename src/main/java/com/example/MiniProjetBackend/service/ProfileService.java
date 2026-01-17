package com.example.MiniProjetBackend.service;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.MiniProjetBackend.dto.EtudiantProfileDTO;
import com.example.MiniProjetBackend.dto.FormateurProfileDTO;
import com.example.MiniProjetBackend.dto.ProfileUpdateRequest;
import com.example.MiniProjetBackend.dto.UserProfileDTO;
import com.example.MiniProjetBackend.entity.Etudiant;
import com.example.MiniProjetBackend.entity.Formateur;
import com.example.MiniProjetBackend.entity.Specialite;
import com.example.MiniProjetBackend.entity.User;
import com.example.MiniProjetBackend.repository.EtudiantRepository;
import com.example.MiniProjetBackend.repository.FormateurRepository;
import com.example.MiniProjetBackend.repository.SpecialiteRepository;
import com.example.MiniProjetBackend.repository.UserRepository;

@Service
public class ProfileService {

    private final UserRepository userRepo;
    private final EtudiantRepository etudiantRepo;
    private final FormateurRepository formateurRepo;
    private final SpecialiteRepository specialiteRepo;

    public ProfileService(UserRepository userRepo,
                          EtudiantRepository etudiantRepo,
                          FormateurRepository formateurRepo,
                          SpecialiteRepository specialiteRepo) {
        this.userRepo = userRepo;
        this.etudiantRepo = etudiantRepo;
        this.formateurRepo = formateurRepo;
        this.specialiteRepo = specialiteRepo;
    }

    // 🔎 GET PROFILE
    public UserProfileDTO getProfile(String email) {

        User user = userRepo.findByEmail(email).orElseThrow();
        String role = user.getRole().getName();

        if (role.equals("ROLE_ETUDIANT")) {

            Etudiant e = etudiantRepo.findByUser(user).orElseThrow();

            return new UserProfileDTO(
                    user.getId(),
                    user.getEmail(),
                    role,
                    e.getNom(),
                    e.getPrenom(),
                    e.getMatricule(),
                    null,
                    null
            );
        }

        if (role.equals("ROLE_FORMATEUR")) {

            Formateur f = formateurRepo.findByUser(user).orElseThrow();

            return new UserProfileDTO(
                    user.getId(),
                    user.getEmail(),
                    role,
                    f.getNom(),
                    null,
                    null,
                    f.getSpecialite() != null ? f.getSpecialite().getId() : null,
                    f.getSpecialite() != null ? f.getSpecialite().getNom() : null
            );
        }
     // ✅ AJOUT ADMIN
        if (role.equals("ROLE_ADMIN")) {
            return new UserProfileDTO(
                    user.getId(),
                    user.getEmail(),
                    role,
                    null, // nom
                    null, // prenom
                    null, // matricule
                    null, // specialiteId
                    null  // specialiteNom
            );
        }

        throw new RuntimeException("Rôle non supporté");
    }

    // ✏️ UPDATE PROFILE
    public void updateProfile(String email, ProfileUpdateRequest req) {

        User user = userRepo.findByEmail(email).orElseThrow();
        String role = user.getRole().getName();

        if (role.equals("ROLE_ETUDIANT")) {

            Etudiant e = etudiantRepo.findByUser(user).orElseThrow();
            e.setNom(req.getNom());
            e.setPrenom(req.getPrenom());
            etudiantRepo.save(e);
        }

        if (role.equals("ROLE_FORMATEUR")) {

            Formateur f = formateurRepo.findByUser(user).orElseThrow();
            f.setNom(req.getNom());

            if (req.getSpecialiteId() != null) {
                Specialite s = specialiteRepo
                        .findById(req.getSpecialiteId())
                        .orElseThrow();
                f.setSpecialite(s);
            }

            formateurRepo.save(f);
        }
    }

}
