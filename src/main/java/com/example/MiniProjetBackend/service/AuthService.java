package com.example.MiniProjetBackend.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.MiniProjetBackend.dto.AuthResponse;
import com.example.MiniProjetBackend.dto.LoginRequest;
import com.example.MiniProjetBackend.entity.User;
import com.example.MiniProjetBackend.repository.UserRepository;
import com.example.MiniProjetBackend.security.JwtUtil;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;

    public AuthService(AuthenticationManager authManager,
                       JwtUtil jwtUtil,
                       UserRepository userRepo) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
    }

    public AuthResponse login(LoginRequest req) {

        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                req.email, req.password
            )
        );

        User user = userRepo.findByEmail(req.email).orElseThrow();

        String token = jwtUtil.generateToken(
            (UserDetails) auth.getPrincipal()
        );

        return new AuthResponse(
            token,
            user.getRole().getName(),
            user.isEnabled()
        );
    }
}

