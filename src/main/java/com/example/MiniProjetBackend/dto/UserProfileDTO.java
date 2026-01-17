package com.example.MiniProjetBackend.dto;

public record UserProfileDTO(
        Long userId,
        String email,
        String role,

        String nom,
        String prenom,
        String matricule,

        Long specialiteId,
        String specialiteNom
) {}


