package com.example.MiniProjetBackend.controller.web;

import com.example.MiniProjetBackend.entity.Salle;
import com.example.MiniProjetBackend.service.SalleService;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/salles")
public class SalleController {

    private final SalleService salleService;

    public SalleController(SalleService salleService) {
        this.salleService = salleService;
    }

    // ✅ LIST + SEARCH + PAGINATION
    @GetMapping
    public String list(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "5") int size,
                       @RequestParam(required = false) String keyword) {

        Page<Salle> salles = salleService.getSallesPage(page, size, keyword);

        model.addAttribute("salles", salles);
        model.addAttribute("keyword", keyword);

        return "admin/salle/list";
    }

    // ✅ JSON : récupérer salle by id (pour modal view + edit)
    @GetMapping("/edit/{id}")
    @ResponseBody
    public Salle getSalleJson(@PathVariable Long id) {
        return salleService.getSalleById(id);
    }

    // ✅ CREATE/UPDATE depuis modal (même endpoint)
    @PostMapping("/update")
    public String update(@RequestParam(required = false) Long id,
                         @RequestParam String nom,
                         @RequestParam Integer capacite) {

        Salle salle = (id == null) ? new Salle() : salleService.getSalleById(id);
        salle.setNom(nom);
        salle.setCapacite(capacite);

        salleService.saveOrUpdate(salle);
        return "redirect:/admin/salles";
    }

    // ✅ DELETE
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        salleService.deleteSalle(id);
        return "redirect:/admin/salles";
    }

    // (Optionnel) Page form séparée si tu veux créer sans modal
    @GetMapping("/new")
    public String newForm() {
        return "admin/salle/form";
    }
}
