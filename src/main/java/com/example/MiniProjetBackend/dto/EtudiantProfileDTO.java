package com.example.MiniProjetBackend.dto;

public record EtudiantProfileDTO(
        String matricule,
        String nom,
        String prenom,
        String email
) {}

