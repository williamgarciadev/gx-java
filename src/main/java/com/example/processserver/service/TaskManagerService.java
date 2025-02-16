package com.example.processserver.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class TaskManagerService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final LinkedBlockingQueue<Map<String, Object>> taskQueue = new LinkedBlockingQueue<>();

    private final ConcurrentHashMap<String, CompletableFuture<Void>> taskFutures = new ConcurrentHashMap<>();

    public String iniciarTarea(String nombreTarea, int userId) {
        String taskId = UUID.randomUUID().toString(); // Generar un ID único para la tarea

        // Guardar la tarea en la tabla TASKS con estado 'PENDING'
        String sqlInsert = "INSERT INTO TASKS (task_id, task_name, task_status, user_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sqlInsert, taskId, nombreTarea, "PENDING", userId);

        return taskId;
    }

    public void encolarTarea(Map<String, Object> tarea) {
        taskQueue.offer(tarea);
    }

    public void procesarTareasEnCola() {
        while (!taskQueue.isEmpty()) {
            Map<String, Object> tarea = taskQueue.poll();
            CompletableFuture<Void> future = procesarTarea(tarea);
            taskFutures.put(tarea.get("task_id").toString(), future);
        }
    }

    public CompletableFuture<Void> procesarTarea(Map<String, Object> tarea) {
        return CompletableFuture.runAsync(() -> {
            // Procesar la tarea
            String taskId = tarea.get("task_id").toString();
            String taskName = tarea.get("task_name").toString();
            int userId = (int) tarea.get("user_id");

            // Actualizar el estado de la tarea a 'IN PROGRESS'
            String sqlUpdate = "UPDATE TASKS SET task_status = ? WHERE task_id = ?";
            jdbcTemplate.update(sqlUpdate, "IN PROGRESS", taskId);

            // Simular el procesamiento de la tarea
            try {
                Thread.sleep(5000); // Simular un proceso que toma 5 segundos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Actualizar el estado de la tarea a 'COMPLETED'
            sqlUpdate = "UPDATE TASKS SET task_status = ? WHERE task_id = ?";
            jdbcTemplate.update(sqlUpdate, "COMPLETED", taskId);

            // Eliminar la tarea del mapa de futuros
            taskFutures.remove(taskId);
        });
    }

    public CompletableFuture<Void> cancelarTarea(String taskId) {
        CompletableFuture<Void> future = taskFutures.get(taskId);
        if (future != null) {
            future.cancel(true);
            taskFutures.remove(taskId);
        }
        return CompletableFuture.completedFuture(null);
    }
}


