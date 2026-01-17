package com.example.MiniProjetBackend.controller.rest;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.MiniProjetBackend.entity.User;
import com.example.MiniProjetBackend.service.UserService;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserRestController {

    private final UserService service;

    public AdminUserRestController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<User>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        Page<User> users = service.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggle(@PathVariable Long id) {
        service.toggleEnabled(id);
        return ResponseEntity.noContent().build();
    }
}

