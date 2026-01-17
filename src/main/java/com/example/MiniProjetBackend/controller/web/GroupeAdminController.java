package com.example.MiniProjetBackend.controller.web;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.MiniProjetBackend.entity.Groupe;
import com.example.MiniProjetBackend.service.GroupeService;

@Controller
@RequestMapping("/admin/groupes")
public class GroupeAdminController {

    private final GroupeService service;


    public GroupeAdminController(GroupeService service) {
        this.service = service;
    }

    // LIST + SEARCH + PAGINATION
    @GetMapping
    public String list(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Page<Groupe> groupes =
                service.search(keyword, PageRequest.of(page, 5));

        model.addAttribute("groupes", groupes);
        model.addAttribute("keyword", keyword);

        return "admin/groupes/list";
    }

    // GET ONE (AJAX)
    @GetMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // SAVE / UPDATE
    @PostMapping("/update")
    public String update(Groupe g) {
        service.save(g);
        return "redirect:/admin/groupes";
    }

    // NEW
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("groupe", new Groupe());
        return "admin/groupes/form";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/admin/groupes";
    }
    

}
