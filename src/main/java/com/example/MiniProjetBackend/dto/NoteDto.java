package com.example.MiniProjetBackend.dto;

public class NoteDto {

	public Long id;
    public Double valeur;
    public String etudiantMatricule;
    public String coursCode;
    public String coursTitre;

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

    public String getEtudiantMatricule() {
        return etudiantMatricule;
    }

    public void setEtudiantMatricule(String etudiantMatricule) {
        this.etudiantMatricule = etudiantMatricule;
    }

    public String getCoursCode() {
        return coursCode;
    }

    public void setCoursCode(String coursCode) {
        this.coursCode = coursCode;
    }
}
