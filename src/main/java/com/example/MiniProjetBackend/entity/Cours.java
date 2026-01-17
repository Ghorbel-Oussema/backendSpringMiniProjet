package com.example.MiniProjetBackend.entity;


import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Cours {

    @Id
    private String code;

    private String titre;
    private String description;

    @ManyToOne
    @JoinColumn(name = "formateur_id")
    @JsonIgnore   // ✅ CRITIQUE

    private Formateur formateur;


    @OneToMany(mappedBy = "cours")
    @JsonIgnore
    private List<Note> notes;
   

    public String getCode() {
        return code;
    }
 
    public void setCode(String code) {
        this.code = code;
    }
 
    public String getTitre() {
        return titre;
    }
 
    public void setTitre(String titre) {
        this.titre = titre;
    }
 
    public String getDescription() {
        return description;
    }
 
    public void setDescription(String description) {
        this.description = description;
    }

    public Formateur getFormateur() {
        return formateur;
    }
 
    public void setFormateur(Formateur formateur) {
        this.formateur = formateur;
    }

    
    public List<Note> getNotes() {
        return notes;
    }
 
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
   

}
