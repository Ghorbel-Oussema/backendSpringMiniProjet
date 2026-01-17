package com.example.MiniProjetBackend.entity;


import jakarta.persistence.*;

@Entity
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double valeur;

    @ManyToOne
    @JoinColumn(name = "etudiant_matricule")
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "cours_code")
    private Cours cours;

    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public Double getValeur() {
        return valeur;
    }
 
    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }
 
    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Cours getCours() {
        return cours;
    }
 
    public void setCours(Cours cours) {
        this.cours = cours;
    }
}
