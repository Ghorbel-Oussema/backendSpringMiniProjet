package com.example.MiniProjetBackend.dto;


import java.time.LocalDate;
import java.time.LocalTime;

public class SeanceDetailsDTO {
    public Long id;
    public LocalDate date;
    public LocalTime heureDebut;
    public LocalTime heureFin;

    public CoursDTO cours;
    public SalleDTO salle;
    public GroupeDTO groupe;

    public static class CoursDTO {
        public String code;
        public String titre; // adapte selon ton entity
        public CoursDTO(String code, String titre) {
            this.code = code; this.titre = titre;
        }
    }

    public static class SalleDTO {
        public Long id;
        public String nom;
        public Integer capacite;
        public SalleDTO(Long id, String nom, Integer capacite) {
            this.id = id; this.nom = nom; this.capacite = capacite;
        }
    }

    public static class GroupeDTO {
        public Long id;
        public String nom; // adapte: nom/libelle...
        public GroupeDTO(Long id, String nom) {
            this.id = id; this.nom = nom;
        }
    }
}

