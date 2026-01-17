package com.example.MiniProjetBackend.controller.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.MiniProjetBackend.entity.Cours;
import com.example.MiniProjetBackend.service.CoursService;
import com.example.MiniProjetBackend.service.FormateurService;
import com.example.MiniProjetBackend.service.SpecialiteService;

@Controller
@RequestMapping("/admin/cours")
public class CoursWebController {

    private final CoursService coursService;
    private final FormateurService formateurService;

    public CoursWebController(CoursService coursService,
                              FormateurService formateurService) {
        this.coursService = coursService;
        this.formateurService = formateurService;
    }

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Page<Cours> cours =
                coursService.search(keyword, PageRequest.of(page, 5));

        model.addAttribute("cours", cours);
        model.addAttribute("keyword", keyword);
        model.addAttribute("formateurs", formateurService.findAll());

        return "admin/cours/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("cours", new Cours());
        model.addAttribute("formateurs", formateurService.findAll());
        return "admin/cours/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Cours cours) {
        coursService.save(cours);
        return "redirect:/admin/cours";
    }

    @GetMapping("/edit/{code}")
    @ResponseBody
    public ResponseEntity<?> getOne(@PathVariable String code) {
        return ResponseEntity.ok(coursService.findByCode(code));
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Cours cours) {
        coursService.save(cours);
        return "redirect:/admin/cours";
    }

    @GetMapping("/delete/{code}")
    public String delete(@PathVariable String code) {
        coursService.delete(code);
        return "redirect:/admin/cours";
    }
}
