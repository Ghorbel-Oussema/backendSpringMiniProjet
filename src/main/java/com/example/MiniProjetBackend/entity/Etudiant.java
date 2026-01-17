package com.example.MiniProjetBackend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Etudiant {

	   @Id
	    private String matricule;

	    private String nom;
	    private String prenom;
	    private LocalDate dateInscription;

	    @OneToOne
	    @JoinColumn(name = "user_id", unique = true)
	    private User user;

	    @ManyToOne
	    @JoinColumn(name = "groupe_id")
	    private Groupe groupe;


	    @OneToMany(mappedBy = "etudiant")
	    @JsonIgnore
	    private List<Note> notes;

    public String getMatricule() {
        return matricule;
    }
 
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
 
    public String getNom() {
        return nom;
    }
 
    public void setNom(String nom) {
        this.nom = nom;
    }
 
    public String getPrenom() {
        return prenom;
    }
 
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
 
   
 
    public LocalDate getDateInscription() {
        return dateInscription;
    }
 
    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Groupe getGroupe() {
        return groupe;
    }
 
    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }
    public User getUser() {
        return user;
    }
 
    public void setUser(User user) {
        this.user = user;
    }

    

    public List<Note> getNotes() {
        return notes;
    }
 
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
