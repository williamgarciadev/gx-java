package com.example.processserver.controller;

import com.example.processserver.dto.AbonadoDto;
import com.example.processserver.service.AbonadoService;
import com.example.processserver.service.ApliqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/abonados")
public class AbonadoController {

    @Autowired
    private AbonadoService abonadoService;

    @Autowired
    private ApliqueService apliqueService;

//    @GetMapping("/excel")
//    public ResponseEntity<InputStreamResource> exportToExcel() {
//        try {
//            long startTime = System.nanoTime();
//
//            ByteArrayInputStream in = abonadoService.generarExcel();
//
//            long endTime = System.nanoTime();
//            long durationInMillis = (endTime - startTime) / 1_000_000; // Convertir a milisegundos
//
//            System.out.println("Tiempo de generación de Excel: " + durationInMillis + " ms");
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Content-Disposition", "attachment; filename=abonados.xlsx");
//
//            return ResponseEntity
//                    .ok()
//                    .headers(headers)
//                    .body(new InputStreamResource(in));
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    @GetMapping("/excel")
    public ResponseEntity<InputStreamResource> exportToExcel() {
        try {
            long startTime = System.nanoTime();

            // Llamada asíncrona para generar el Excel
            CompletableFuture<ByteArrayInputStream> excelFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return abonadoService.generarExcel().get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });

            // Obtener el resultado de forma sincrónica aquí si necesitas el archivo de inmediato
            ByteArrayInputStream in = excelFuture.get();

            long endTime = System.nanoTime();
            long durationInMillis = (endTime - startTime) / 1_000_000; // Convertir a milisegundos
            System.out.println("Tiempo de generación de Excel: " + durationInMillis + " ms");

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=abonados.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(new InputStreamResource(in));

        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/procesar-abonados")
    public String procesarAbonados() throws Exception {
        // Llamar al servicio asíncrono para procesar los abonados en paralelo
        CompletableFuture<Void> future = abonadoService.procesarAbonados();

        // Esperar a que todos los hilos terminen de procesar
        future.get();

        return "Procesamiento de abonados completado";
    }
    // Recuperar los datos de los abonados
    @GetMapping("/aboandos")
    public List<AbonadoDto> getAbonados() {
        // saber cuanto tiempo tarda en obtener los abonados
        long startTime = System.nanoTime();

        // log de los aboandos
        System.out.println("Obteniendo los abonados" );
        // Llamar al servicio para obtener los abonados
        List<AbonadoDto> abonados = abonadoService.listarAbonados();
        // log de los abonados
        System.out.println("Abonados obtenidos: " + abonados.size());
        // iterar los abonados
        for (AbonadoDto abonado : abonados) {
            System.out.println(abonado.getAboCod());
        }
        // saber cuanto tiempo tarda en obtener los abonados
        long endTime = System.nanoTime();
        long durationInMillis = (endTime - startTime) / 1_000_000; // Convertir a milisegundos
        System.out.println("Tiempo de obtención de abonados: " + durationInMillis + " ms");
        // Retornar los abonados
        return abonadoService.listarAbonados();
    }


}
