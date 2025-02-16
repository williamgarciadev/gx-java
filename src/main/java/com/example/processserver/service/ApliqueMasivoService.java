package com.example.processserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ApliqueMasivoService {

    @Autowired
    private ApliqueAsyncService apliqueAsyncService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Iniciar la tarea de aplique masivo
    public String iniciarTareaMasiva(int userId) {
        String taskId = UUID.randomUUID().toString();
        String sqlInsert = "INSERT INTO TASKS (task_id, task_name, task_status, user_id) VALUES (?, 'Aplique Masivo', 'RUNNING', ?)";
        jdbcTemplate.update(sqlInsert, taskId, userId);
        return taskId;
    }

    public void actualizarEstadoTarea(String taskId, String status, String result) {
        String sqlUpdate = "UPDATE TASKS SET task_status = ?, result = ? WHERE task_id = ?";
        jdbcTemplate.update(sqlUpdate, status, result, taskId);
    }

    @Transactional
    public CompletableFuture<Void> procesarAbonadosMasivamente(String taskId) {
        List<Integer> abonados = obtenerTodosLosAbonados();
        actualizarEstadoTarea(taskId, "RUNNING", null);

        // Crear una lista de CompletableFuture
        List<CompletableFuture<Void>> futures = abonados.stream()
                .map(aboCod -> {
                    if (esTareaCancelada(taskId)) {
                        CompletableFuture<Void> cancelledFuture = new CompletableFuture<>();
                        cancelledFuture.completeExceptionally(new RuntimeException("Tarea cancelada: " + taskId));
                        return cancelledFuture;
                    }
                    return apliqueAsyncService.procesarDocumentosPorAbonado(aboCod, taskId);
                })
                .collect(Collectors.toList());

        // Combinar todos los futures
        CompletableFuture<Void> allFutures = CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> actualizarEstadoTarea(taskId, "COMPLETED", "Tarea completada con éxito"))
                .exceptionally(ex -> {
                    actualizarEstadoTarea(taskId, "FAILED", "Error o tarea cancelada: " + ex.getMessage());
                    return null;
                });

        return allFutures;
    }

    private boolean esTareaCancelada(String taskId) {
        String sql = "SELECT task_status FROM TASKS WHERE task_id = ?";
        String estado = jdbcTemplate.queryForObject(sql, new Object[]{taskId}, String.class);
        return "CANCELED".equals(estado);
    }

    private List<Integer> obtenerTodosLosAbonados() {
        String sql = "SELECT AboCod FROM ABONADOS WHERE ABOSTS = 'X' ORDER BY AboCod";
        return jdbcTemplate.queryForList(sql, Integer.class);
    }

    public String obtenerEstadoTarea(String taskId) {
        String sql = "SELECT task_status FROM TASKS WHERE task_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{taskId}, String.class);
    }
}

//package com.example.processserver.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.UUID;
//import java.util.concurrent.CompletableFuture;
//
//@Service
//public class ApliqueMasivoService {
//
//    @Autowired
//    private ApliqueAsyncService apliqueAsyncService; // Inyección del servicio asíncrono
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    // Iniciar la tarea de aplique masivo
//    public String iniciarTareaMasiva(int userId) {
//        String taskId = UUID.randomUUID().toString();  // Generar un ID único para la tarea
//        String sqlInsert = "INSERT INTO TASKS (task_id, task_name, task_status, user_id) VALUES (?, 'Aplique Masivo', 'RUNNING', ?)";
//        jdbcTemplate.update(sqlInsert, taskId, userId);
//        return taskId;
//    }
//
//    public void actualizarEstadoTarea(String taskId, String status, String result) {
//        String sqlUpdate = "UPDATE TASKS SET task_status = ?, result = ? WHERE task_id = ?";
//        jdbcTemplate.update(sqlUpdate, status, result, taskId);
//    }
//
//    // Procesar los abonados masivamente
//    public CompletableFuture<Void> procesarAbonadosMasivamente(String taskId) {
//        try {
//            // Obtener la lista de abonados
//            List<Integer> abonados = obtenerTodosLosAbonados();
//
//            // Marcar la tarea como "RUNNING" y procesar abonados
//            actualizarEstadoTarea(taskId, "RUNNING", null);
//
//            // Procesos iniciales
//            deleteDocDetForAbonado();
//            actualizarDocumentosEstado();
//            truncateProcesoAplicacion();
//
//            // Procesar cada abonado en paralelo
//            for (Integer aboCod : abonados) {
//                // Verificar si la tarea ha sido cancelada antes de procesar
//                if (esTareaCancelada(taskId)) {
//                    System.out.println("Tarea cancelada: " + taskId);
//                    actualizarEstadoTarea(taskId, "CANCELED", "Tarea cancelada por el usuario");
//                    return CompletableFuture.completedFuture(null);
//                }
//                apliqueAsyncService.procesarDocumentosPorAbonado(aboCod, taskId); // Llamada al nuevo servicio asíncrono
//            }
//
//            // Actualizar el estado de la tarea a "COMPLETED" al finalizar
//            actualizarEstadoTarea(taskId, "COMPLETED", "Tarea completada con éxito");
//        } catch (Exception e) {
//            // Manejo de excepciones para actualizar el estado de la tarea en caso de error
//            actualizarEstadoTarea(taskId, "FAILED", "Error durante la ejecución: " + e.getMessage());
//        }
//
//        return CompletableFuture.completedFuture(null);
//    }
//
//    // Método para verificar si la tarea ha sido cancelada
//    private boolean esTareaCancelada(String taskId) {
//        String sql = "SELECT task_status FROM TASKS WHERE task_id = ?";
//        String estado = jdbcTemplate.queryForObject(sql, new Object[]{taskId}, String.class);
//        return "CANCELED".equals(estado);
//    }
//
//    // Obtener la lista de abonados desde la base de datos
//    public List<Integer> obtenerTodosLosAbonados() {
//        String sql = "SELECT AboCod FROM ABONADOS ORDER BY AboCod";
//
//        List<Integer> abonados = jdbcTemplate.queryForList(sql, Integer.class);
//        if (abonados == null || abonados.isEmpty()) {
//            return List.of();  // Devuelve una lista vacía si no hay resultados
//        }
//        return abonados;
//    }
//
//    // Método para eliminar los registros en DOCDET para un abonado específico
//    private void deleteDocDetForAbonado() {
//        String deleteSql = "DELETE FROM DOCDET";
//        jdbcTemplate.update(deleteSql);
//        System.out.println("Registros eliminados de DOCDET");
//    }
//
//    // Método para truncar la tabla PROCESO_APLICACION
//    private void truncateProcesoAplicacion() {
//        String truncateSql = "TRUNCATE TABLE PROCESO_APLICACION";
//        jdbcTemplate.update(truncateSql);
//        System.out.println("Tabla PROCESO_APLICACION truncada");
//    }
//
//    // Método para actualizar el estado de los documentos
//    public void actualizarDocumentosEstado() {
//        String sqlUpdate = "UPDATE D\n" +
//                "SET DocSts = 'I',\n" +
//                "    DocImpApl = 0\n" +
//                "FROM datoscabledb.dbo.DOCUMENT D\n" +
//                "INNER JOIN datoscabledb.dbo.TIPOSDOC tp ON tp.doctpo = D.DocTpo\n" +
//                "WHERE DocFlgTpo IN ('S', 'M', 'A', 'T');";
//
//        jdbcTemplate.update(sqlUpdate);
//        System.out.println("Estado de documentos actualizado en hilo: " + Thread.currentThread().getName());
//    }
//}
