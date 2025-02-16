package com.example.processserver.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class TareaService {

    @Async("taskExecutor")
    public CompletableFuture<Void> procesarTareaEnParalelo(List<Integer> datos) {
        int numeroDeHilos = 4;  // Puedes ajustar el número de hilos según el tamaño de los datos y recursos
        int tamanioPorTarea = datos.size() / numeroDeHilos;

        ExecutorService executorService = Executors.newFixedThreadPool(numeroDeHilos);

        List<Future<Void>> futures = new ArrayList<>();

        for (int i = 0; i < numeroDeHilos; i++) {
            int desde = i * tamanioPorTarea;
            int hasta = (i == numeroDeHilos - 1) ? datos.size() : (i + 1) * tamanioPorTarea;
            List<Integer> subLista = datos.subList(desde, hasta);

            // Enviar cada subtarea a ser ejecutada en paralelo
            Future<Void> future = executorService.submit(() -> {
                procesarSubTarea(subLista); // Método que procesa la subtarea
                return null;
            });

            futures.add(future);
        }

        // Esperar a que todas las subtareas terminen
        for (Future<Void> future : futures) {
            try {
                future.get(); // Esperar hasta que termine cada hilo
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();

        return CompletableFuture.completedFuture(null);
    }

    // Simulación de procesamiento de una subtarea
    public void procesarSubTarea(List<Integer> subLista) {
        for (Integer dato : subLista) {
            System.out.println("Procesando dato: " + dato + " en hilo " + Thread.currentThread().getName());
            // Aquí puedes agregar la lógica de procesamiento que quieras para cada dato
        }
    }
}
