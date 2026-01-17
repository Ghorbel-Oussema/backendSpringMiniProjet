package com.example.MiniProjetBackend.dto;

public class InscriptionDto {

    private Long id;
    private String etudiantMatricule;
    private String coursCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
