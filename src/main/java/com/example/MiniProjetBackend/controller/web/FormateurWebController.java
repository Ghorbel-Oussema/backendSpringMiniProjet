package com.example.MiniProjetBackend.controller.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.MiniProjetBackend.entity.Etudiant;
import com.example.MiniProjetBackend.entity.Formateur;
import com.example.MiniProjetBackend.service.FormateurService;
import com.example.MiniProjetBackend.service.SpecialiteService;

@Controller
@RequestMapping("/admin/formateurs")
public class FormateurWebController {

    private final FormateurService service;
    private final SpecialiteService SpecialiteService;


    public FormateurWebController(FormateurService service,SpecialiteService SpecialiteService) {
        this.service = service;
        this.SpecialiteService =SpecialiteService;
    }

    // LIST + SEARCH + PAGINATION
    @GetMapping
    public String list(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Page<Formateur> formateurs =
                service.search(keyword, PageRequest.of(page, 5));

        model.addAttribute("formateurs", formateurs);
        model.addAttribute("specialites", SpecialiteService.findAll());

        model.addAttribute("keyword", keyword);
        

        return "admin/formateurs/list";
    }

    // GET ONE (AJAX)
    @GetMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getDtoById(id));
    }


    // SAVE / UPDATE
    @PostMapping("/update")
    public String update(Formateur f) {
        service.save(f);
        return "redirect:/admin/formateurs";
    }
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("formateurs", new Formateur());
        return "admin/formateurs/form";
    }


    // DELETE
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/admin/formateurs";
    }
}
