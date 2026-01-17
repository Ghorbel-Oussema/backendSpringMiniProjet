package com.example.MiniProjetBackend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class SeanceDto {
    public Long id;
    public LocalDate date;
    public LocalTime heureDebut;
    public LocalTime heureFin;

    public Long salleId;
    public String salleNom;

    public String coursCode;
    public String coursTitre;

    public Long formateurId;
    public String formateurNom;

    public Long groupeId;
    public String groupeNom;
}
