package com.example.MiniProjetBackend.dto;
public class CoursDto {

	public String code;
	public String titre;
	public String description;
    public String formateurNom;
    public String SpecialiteNom;


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

    public String getFormateurNom() {
        return formateurNom;
    }

    public void setFormateurNom(String formateurNom) {
        this.formateurNom = formateurNom;
    }
    public String getSpecialiteNom() {
        return SpecialiteNom;
    }

    public void setSpecialiteNom(String SpecialiteNom) {
        this.SpecialiteNom = SpecialiteNom;
    }
}
