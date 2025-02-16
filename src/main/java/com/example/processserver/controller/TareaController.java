package com.example.processserver.controller;

import com.example.processserver.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @GetMapping("/procesar-tarea")
    public String procesarTarea() throws Exception {
        List<Integer> datos = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10); // Datos de ejemplo

        // Llamar a la tarea que se procesa en varios hilos
        CompletableFuture<Void> future = tareaService.procesarTareaEnParalelo(datos);

        // Esperar hasta que el procesamiento en paralelo haya finalizado
        future.get();

        return "Tarea procesada en paralelo";
    }
}
