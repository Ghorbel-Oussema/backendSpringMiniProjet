package com.example.MiniProjetBackend.controller.rest;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.MiniProjetBackend.dto.LoginRequest;
import com.example.MiniProjetBackend.dto.RegisterRequest;
import com.example.MiniProjetBackend.service.AuthService;
import com.example.MiniProjetBackend.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("http://localhost:4200")
public class AuthRestController {

    private final AuthService authService;
    private final UserService userService;

    public AuthRestController(AuthService authService,
                              UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    // 🔐 LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    // 📝 REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {

        userService.createUser(
            req.email,
            req.password,
            req.role
        );

        return ResponseEntity.ok(
            Map.of("message", "Compte créé. En attente d'activation")
        );
    }
 // 🚪 LOGOUT (JWT)
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {

        // 🔴 Rien à faire côté serveur en JWT
        // 🔴 Le frontend supprime le token

        return ResponseEntity.ok(
            Map.of("message", "Déconnexion réussie")
        );
    }

}

