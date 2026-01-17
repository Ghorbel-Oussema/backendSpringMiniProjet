package com.example.MiniProjetBackend.controller.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.MiniProjetBackend.service.DashboardService;

@RestController
@RequestMapping("/api/admin")
public class AdminDashboardRestController {

    private final DashboardService dashboardService;

    public AdminDashboardRestController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard() {

        Map<String, Object> res = new HashMap<>();
        res.put("stats", dashboardService.globalStats());
        res.put("coursParFormateur", dashboardService.coursParFormateur());
        res.put("etudiantsParGroupe", dashboardService.etudiantsParGroupe());
        res.put("coursParSpecialite", dashboardService.coursParSpecialite());

        return ResponseEntity.ok(res);
    }
}
