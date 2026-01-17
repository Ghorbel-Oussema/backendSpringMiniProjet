package com.example.MiniProjetBackend.controller.web;


import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.MiniProjetBackend.service.DashboardService;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final DashboardService dashboardService;

    public AdminController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public String dashboard(Model model) {

        Map<String, Long> stats = dashboardService.globalStats();
        System.out.println("STATS = " + stats);

        System.out.println("Cours/Formateur = "
            + dashboardService.coursParFormateur().size());

        model.addAttribute("stats", stats);
        model.addAttribute("coursParFormateur", dashboardService.coursParFormateur());
        model.addAttribute("etudiantsParGroupe", dashboardService.etudiantsParGroupe());
        model.addAttribute("coursParSpecialite", dashboardService.coursParSpecialite());

        return "admin/dashboard";
    }

}
