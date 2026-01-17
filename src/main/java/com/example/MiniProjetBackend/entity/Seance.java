package com.example.MiniProjetBackend.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Seance {

    @Id @GeneratedValue
    private Long id;

    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    @ManyToOne
    @JoinColumn(name = "salle_id")
    private Salle salle;


    @ManyToOne
    private Cours cours;
    @ManyToOne
    @JoinColumn(name = "groupe_id")
    private Groupe groupe;


	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
	// 🔴 OBLIGATOIRE
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public LocalTime getHeureDebut() {
		return heureDebut;
	}

	public void setHeureDebut(LocalTime heureDebut) {
		this.heureDebut = heureDebut;
	}

	public LocalTime getHeureFin() {
		return heureFin;
	}

	public void setHeureFin(LocalTime heureFin) {
		this.heureFin = heureFin;
	}

	public Salle getSalle() { return salle; }
	public void setSalle(Salle salle) { this.salle = salle; }

	public Groupe getGroupe() { return groupe; }
	public void setGroupe(Groupe groupe) { this.groupe = groupe; }

	 public Cours getCours() {
	        return cours;
	    }
	 
	    public void setCours(Cours cours) {
	        this.cours = cours;
	    }
}
