package com.example.MiniProjetBackend.dto;

public record FormateurProfileDTO(
	    Long id,
	    String nom,
	    String email,
	    Long specialiteId,
	    String specialiteNom
	) {}
