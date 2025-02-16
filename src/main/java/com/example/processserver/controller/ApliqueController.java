package com.example.processserver.controller;

import com.example.processserver.service.ApliqueMasivoService;
import com.example.processserver.service.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayInputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class ApliqueController {

    @Autowired
    private ApliqueMasivoService apliqueMasivoService;

    @Autowired
    private ExcelExportService excelService;

    private final ConcurrentHashMap<String, CompletableFuture<Void>> activeTasks = new ConcurrentHashMap<>();

    @GetMapping("/aplique-masivo/{userId}")
    public ResponseEntity<String> iniciarApliqueMasivo(@PathVariable int userId) {
        String taskId = apliqueMasivoService.iniciarTareaMasiva(userId);

        // Calcular tiempo de ejecución
        long startTime = System.currentTimeMillis();
        CompletableFuture<Void> future = apliqueMasivoService.procesarAbonadosMasivamente(taskId);
        activeTasks.put(taskId, future);
        
        // log inicia tarea
        System.out.println("taskId = " + taskId);

        future.whenComplete((result, ex) -> {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            // duracion en segundos
            duration = duration / 1000;

            apliqueMasivoService.actualizarEstadoTarea(taskId, "COMPLETED", "Duración: " + duration + " s");
        });



        // Opcional: manejar la finalización de la tarea para removerla del mapa
        future.whenComplete((result, ex) -> activeTasks.remove(taskId));


        return ResponseEntity.ok("Tarea iniciada con ID: " + taskId);
    }

    @PostMapping("/cancelar-tarea/{taskId}")
    public ResponseEntity<String> cancelarTarea(@PathVariable String taskId) {
        CompletableFuture<Void> future = activeTasks.get(taskId);
        if (future != null) {
            boolean cancelado = future.cancel(true);
            apliqueMasivoService.actualizarEstadoTarea(taskId, "CANCELED", "Cancelada manualmente");
            return ResponseEntity.ok(cancelado ? "Tarea cancelada" : "No se pudo cancelar la tarea");
        }
        return ResponseEntity.badRequest().body("Tarea no encontrada o ya completada");
    }

    @GetMapping("/descargar-excel/{taskId}")
    public ResponseEntity<InputStreamResource> descargarExcel(@PathVariable String taskId) throws Exception {
        String estado = apliqueMasivoService.obtenerEstadoTarea(taskId);
        if (!"COMPLETED".equals(estado)) {
            return ResponseEntity.badRequest().body(null);
        }

        ByteArrayInputStream excel = excelService.generarExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=resultado_aplique.xlsx");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(excel));
    }
}


//    @GetMapping("/procesar-y-generar-excel")
//    public ResponseEntity<InputStreamResource> procesarYGenerarExcel() throws Exception {
//        // Procesa los abonados en paralelo
//        CompletableFuture<Void> future = abonadoService.procesarAbonados();
//
//        // Espera a que todas las tareas de procesamiento terminen
//        future.get();
//
//        // Genera el archivo Excel con los resultados
//        ByteArrayInputStream excel = excelService.generarExcel();
//
//        // Configurar el encabezado para la descarga del archivo
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "attachment; filename=resultado_aplique.xlsx");
//
//        return ResponseEntity
//                .ok()
//                .headers(headers)
//                .body(new InputStreamResource(excel));
//    }


