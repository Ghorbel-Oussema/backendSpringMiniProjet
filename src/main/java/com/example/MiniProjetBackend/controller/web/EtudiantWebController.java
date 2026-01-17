package com.example.MiniProjetBackend.controller.web;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.MiniProjetBackend.entity.Etudiant;
import com.example.MiniProjetBackend.repository.GroupeRepository;
import com.example.MiniProjetBackend.service.EtudiantService;

@Controller
@RequestMapping("/admin/etudiants")
public class EtudiantWebController {

    private final EtudiantService etudiantService;
    private final GroupeRepository groupeRepo;

    public EtudiantWebController(EtudiantService etudiantService,
            GroupeRepository groupeRepo) {
this.etudiantService = etudiantService;
this.groupeRepo = groupeRepo;
}

    // LISTE
    @GetMapping
    public String list(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Page<Etudiant> etudiants =
                etudiantService.search(keyword, PageRequest.of(page, 5));

        model.addAttribute("etudiants", etudiants);
        model.addAttribute("keyword", keyword);
        model.addAttribute("groupes", groupeRepo.findAll());

        return "admin/etudiants/list";
    }

    // FORM AJOUT
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("etudiant", new Etudiant());
        return "admin/etudiants/form";
    }

    // SAVE
    @PostMapping
    public String save(@ModelAttribute Etudiant etudiant) {
        etudiantService.save(etudiant);
        return "redirect:/admin/etudiants";
    }

    // DELETE
    @GetMapping("/delete/{matricule}")
    public String delete(@PathVariable String matricule) {
        etudiantService.delete(matricule);
        return "redirect:/admin/etudiants";
    }
    @GetMapping("/edit/{matricule}")
    @ResponseBody
    public ResponseEntity<?> getOne(@PathVariable String matricule) {
        Etudiant e = etudiantService.findByMatricule(matricule);
        if (e == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(e);
    }


    @PostMapping("/update")
    public String update(
            @RequestParam String matricule,
            @RequestParam String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) Long groupeId
    ) {
        Etudiant e = etudiantService.findByMatricule(matricule);
        e.setNom(nom);
        e.setPrenom(prenom);
        etudiantService.save(e);

        // ✅ affectation (ou désaffectation)
        etudiantService.affecterGroupe(matricule, groupeId);

        return "redirect:/admin/etudiants";
    }


}
