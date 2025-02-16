package com.example.processserver.controller;

import com.example.processserver.service.ApliqueMasivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private ApliqueMasivoService apliqueMasivoService;

    @PostMapping("/cancel/{taskId}")
    public ResponseEntity<String> cancelarTarea(@PathVariable String taskId) {
        apliqueMasivoService.actualizarEstadoTarea(taskId, "CANCELED", "Tarea cancelada por el usuario");
        return ResponseEntity.ok("Tarea cancelada exitosamente: " + taskId);
    }
}

