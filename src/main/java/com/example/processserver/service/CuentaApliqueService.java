package com.example.processserver.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

@Service
public class CuentaApliqueService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Async("taskExecutor")  // Procesa en múltiples hilos
    public void procesarApliqueDeCuenta(int abonadoId) {
        System.out.println("Iniciando aplique de cuenta para el abonado: " + abonadoId + " en hilo " + Thread.currentThread().getName());



        // Inserta el resultado en la tabla PROCESO_APLICACION
        String resultado = "Éxito-" + Thread.currentThread().getName();
        LocalDateTime fechaProcesamiento = LocalDateTime.now();

        String sql = "INSERT INTO PROCESO_APLICACION (abonado_id, resultado, fecha_procesamiento) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, abonadoId, resultado, fechaProcesamiento);

        System.out.println("Finalizando aplique de cuenta para el abonado: " + abonadoId);
    }
}
