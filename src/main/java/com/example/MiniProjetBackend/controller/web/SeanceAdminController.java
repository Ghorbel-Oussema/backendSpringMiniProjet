package com.example.MiniProjetBackend.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.MiniProjetBackend.entity.Cours;
import com.example.MiniProjetBackend.entity.Salle;
import com.example.MiniProjetBackend.entity.Seance;
import com.example.MiniProjetBackend.service.CoursService;
import com.example.MiniProjetBackend.service.GroupeService;
import com.example.MiniProjetBackend.service.SalleService;
import com.example.MiniProjetBackend.service.SeanceService;

@Controller
@RequestMapping("/admin/seances")
public class SeanceAdminController {

    @Autowired
    private SeanceService service;

    @Autowired
    private CoursService coursService;

    @Autowired
    private SalleService salleService;
    @Autowired
    private GroupeService groupeService;


    @GetMapping
    public String list(@RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {

        Page<Seance> seances = service.search(keyword, PageRequest.of(page, 5));

        model.addAttribute("seances", seances);
        model.addAttribute("keyword", keyword);
        model.addAttribute("cours", coursService.findAll());
        model.addAttribute("salles", salleService.findAll()); // ✅ important
        model.addAttribute("groupes", groupeService.findAll());

        return "admin/seances/list";
    }

    // AJAX get one
    @GetMapping("/edit/{id}")
    @ResponseBody
    public Seance getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    // UPDATE (important: recevoir cours.code et salle.id)
    @PostMapping("/update")
    public String update(@RequestParam(required = false) Long id,
                         @RequestParam String date,
                         @RequestParam String heureDebut,
                         @RequestParam String heureFin,
                         @RequestParam(name="cours.code", required = false) String coursCode,
                         @RequestParam(name="salle.id", required = false) Long salleId,
                         @RequestParam(name="groupe.id", required = false) Long groupeId) {

        Seance s = (id == null) ? new Seance() : service.findById(id);

        s.setDate(java.time.LocalDate.parse(date));
        s.setHeureDebut(java.time.LocalTime.parse(heureDebut));
        s.setHeureFin(java.time.LocalTime.parse(heureFin));

        if (coursCode != null && !coursCode.isBlank()) s.setCours(coursService.findById(coursCode));
        else s.setCours(null);

        if (salleId != null) s.setSalle(salleService.getSalleById(salleId));
        else s.setSalle(null);

        if (groupeId != null) s.setGroupe(groupeService.findById(groupeId));
        else s.setGroupe(null);

        service.save(s);
        return "redirect:/admin/seances";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/admin/seances";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("seance", new Seance());
        model.addAttribute("cours", coursService.findAll());
        model.addAttribute("salles", salleService.findAll());
        model.addAttribute("groupes", groupeService.findAll());

        return "admin/seances/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam String date,
                       @RequestParam String heureDebut,
                       @RequestParam String heureFin,
                       @RequestParam(name="cours.code", required = false) String coursCode,
                       @RequestParam(name="salle.id", required = false) Long salleId,
                       @RequestParam(name="groupe.id", required = false) Long groupeId) {

        Seance s = new Seance();
        s.setDate(java.time.LocalDate.parse(date));
        s.setHeureDebut(java.time.LocalTime.parse(heureDebut));
        s.setHeureFin(java.time.LocalTime.parse(heureFin));

        if (coursCode != null && !coursCode.isBlank()) s.setCours(coursService.findById(coursCode));
        if (salleId != null) s.setSalle(salleService.getSalleById(salleId));
        if (groupeId != null) s.setGroupe(groupeService.findById(groupeId));

        service.save(s);
        return "redirect:/admin/seances";
    }

}
