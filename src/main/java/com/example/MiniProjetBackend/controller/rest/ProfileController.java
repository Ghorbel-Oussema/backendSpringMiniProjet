package com.example.MiniProjetBackend.controller.rest;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.MiniProjetBackend.dto.ProfileUpdateRequest;
import com.example.MiniProjetBackend.service.ProfileService;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // 🔎 GET PROFILE
    @GetMapping
    public ResponseEntity<?> myProfile(Authentication auth) {

        String email = auth.getName(); // ✅ email extrait du JWT
        return ResponseEntity.ok(profileService.getProfile(email));
    }

    // ✏️ UPDATE PROFILE
    @PutMapping
    public ResponseEntity<?> updateProfile(
            Authentication auth,
            @RequestBody ProfileUpdateRequest req) {

        profileService.updateProfile(auth.getName(), req);

        return ResponseEntity.ok(
            Map.of("message", "Profil mis à jour avec succès")
        );
    }
}
