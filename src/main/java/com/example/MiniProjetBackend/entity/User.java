package com.example.MiniProjetBackend.entity;


import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email; // 🔴 email au lieu de username

    @Column(nullable = false)
    private String password;

    private boolean enabled = false;

    // 🔴 UNE SEULE RELATION
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    // ===== getters & setters =====
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }
 
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Role getRole() {
        return role;
    }
 
    public void setRole(Role role) {
        this.role = role;
    }
}
