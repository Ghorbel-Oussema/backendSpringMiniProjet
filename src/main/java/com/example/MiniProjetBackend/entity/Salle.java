package com.example.MiniProjetBackend.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;       // ex: "A1", "Salle 204"
    private Integer capacite;

    // optionnel : si tu veux Salle -> Seances
    @OneToMany(mappedBy = "salle")
    @JsonIgnore
    private List<Seance> seances;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Integer getCapacite() { return capacite; }
    public void setCapacite(Integer capacite) { this.capacite = capacite; }
}
