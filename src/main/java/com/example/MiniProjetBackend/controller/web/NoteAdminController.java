package com.example.MiniProjetBackend.controller.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.MiniProjetBackend.entity.Note;
import com.example.MiniProjetBackend.entity.Seance;
import com.example.MiniProjetBackend.service.CoursService;
import com.example.MiniProjetBackend.service.EtudiantService;
import com.example.MiniProjetBackend.service.NoteService;

@Controller
@RequestMapping("/admin/notes")
public class NoteAdminController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private CoursService coursService;

    // LIST + PAGINATION
    @GetMapping
    public String list(
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Page<Note> notes =
                noteService.findAll(PageRequest.of(page, 5));

        model.addAttribute("notes", notes);
        return "admin/notes/list";
    }

    // GET ONE (AJAX)
    @GetMapping("/edit/{id}")
    @ResponseBody
    public Note getOne(@PathVariable Long id) {
        return noteService.findById(id);
    }

    // UPDATE
    @PostMapping("/update")
    public String update(Note note) {
        noteService.save(note);
        return "redirect:/admin/notes";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        noteService.delete(id);
        return "redirect:/admin/notes";
    }

    // FORM NEW
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("note", new Note());
        model.addAttribute("etudiants", etudiantService.findAll());
        model.addAttribute("cours", coursService.findAll());
        return "admin/notes/form";
    }
}
