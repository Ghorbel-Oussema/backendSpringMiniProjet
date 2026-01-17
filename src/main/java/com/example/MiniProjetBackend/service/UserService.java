package com.example.MiniProjetBackend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.MiniProjetBackend.entity.Etudiant;
import com.example.MiniProjetBackend.entity.Formateur;
import com.example.MiniProjetBackend.entity.Role;
import com.example.MiniProjetBackend.entity.User;
import com.example.MiniProjetBackend.repository.EtudiantRepository;
import com.example.MiniProjetBackend.repository.FormateurRepository;
import com.example.MiniProjetBackend.repository.RoleRepository;
import com.example.MiniProjetBackend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final FormateurRepository formateurRepository;
    private final EtudiantRepository EtudiantRepository;


    public UserService(UserRepository userRepo,
                       RoleRepository roleRepo,
                       PasswordEncoder encoder,
                       FormateurRepository formateurRepository,
                       EtudiantRepository EtudiantRepository) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
        this.formateurRepository =formateurRepository;
        this.EtudiantRepository =EtudiantRepository ;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }
    public Page<User> findAll(Pageable pageable) {
        return userRepo.findAll(pageable);
    }

    public void createUser(String email,
                           String password,
                           String roleName) {

        Role role = roleRepo.findByName(roleName)
                .orElseThrow();

        User u = new User();
        u.setEmail(email);
        u.setPassword(encoder.encode(password));
        u.setRole(role);
        u.setEnabled(false); // 🔴 en attente admin

        userRepo.save(u);
    }

    public void toggleEnabled(Long id) {
        User u = userRepo.findById(id).orElseThrow();
        u.setEnabled(!u.isEnabled());
        userRepo.save(u);
        String role = u.getRole().getName();

        if (role.equals("ROLE_ETUDIANT")) {
            createEtudiant(u);
        }

        if (role.equals("ROLE_FORMATEUR")) {
            createFormateur(u);
        }
    }
    private void createEtudiant(User user) {

        if (EtudiantRepository.findByUser(user).isPresent()) return;

        Etudiant e = new Etudiant();
        e.setMatricule("ETU-" + user.getId());
        e.setNom("");
        e.setPrenom("");
        e.setDateInscription(LocalDate.now());
        e.setUser(user);

        EtudiantRepository.save(e);
    }

    private void createFormateur(User user) {

        if (formateurRepository.findByUser(user).isPresent()) return;

        Formateur f = new Formateur();
        f.setNom("");
        f.setUser(user);

        formateurRepository.save(f);
    }
    
}

