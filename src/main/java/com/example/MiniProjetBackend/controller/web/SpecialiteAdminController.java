package com.example.MiniProjetBackend.controller.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.MiniProjetBackend.entity.Specialite;
import com.example.MiniProjetBackend.service.SpecialiteService;

@Controller
@RequestMapping("/admin/specialites")
public class SpecialiteAdminController {

    private final SpecialiteService service;

    public SpecialiteAdminController(SpecialiteService service) {
        this.service = service;
    }

    // LIST + SEARCH + PAGINATION
    @GetMapping
    public String list(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Page<Specialite> specialites =
                service.search(keyword, PageRequest.of(page, 5));

        model.addAttribute("specialites", specialites);
        model.addAttribute("keyword", keyword);
        return "admin/specialites/list";
    }

    // GET ONE (AJAX)
    @GetMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        Specialite s = service.findById(id);
        return s == null ? ResponseEntity.notFound().build()
                         : ResponseEntity.ok(s);
    }

    // CREATE
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("specialite", new Specialite());
        return "admin/specialites/form";
    }

    // SAVE / UPDATE
    @PostMapping("/update")
    public String update(Specialite s) {
        service.save(s);
        return "redirect:/admin/specialites";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/admin/specialites";
    }
}
