package com.example.MiniProjetBackend.controller.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.MiniProjetBackend.entity.User;
import com.example.MiniProjetBackend.service.UserService;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService service;

    public AdminUserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            Model model
    ) {
        Page<User> users = service.findAll(
                PageRequest.of(page, size, Sort.by("id").descending())
        );

        model.addAttribute("users", users);
        model.addAttribute("page", page);
        model.addAttribute("size", size);

        return "admin/users/list";
    }

    @GetMapping("/toggle/{id}")
    public String toggle(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        service.toggleEnabled(id);
        return "redirect:/admin/users?page=" + page + "&size=" + size;
    }
}
