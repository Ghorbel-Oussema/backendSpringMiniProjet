package com.example.MiniProjetBackend.dto;

public class FormateurDto {

	public Long id;
	public String nom;
	public String email;
    public Long specialiteId;
    public String specialiteNom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	public Long getSpecialiteId() {
		return specialiteId;
	}

	public void setSpecialiteId(Long specialiteId) {
		this.specialiteId = specialiteId;
	}

	public String getSpecialiteNom() {
		return specialiteNom;
	}

	public void setSpecialiteNom(String specialiteNom) {
		this.specialiteNom = specialiteNom;
	}
}
