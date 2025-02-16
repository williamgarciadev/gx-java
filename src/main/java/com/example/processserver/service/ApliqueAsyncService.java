package com.example.processserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class ApliqueAsyncService {

    private static final Logger logger = LoggerFactory.getLogger(ApliqueAsyncService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Async("taskExecutor")
    public CompletableFuture<Void> procesarDocumentosPorAbonado(int aboCod, String taskId) {
        logger.info("Inicio procesarDocumentosPorAbonado para AboCod: {} en hilo: {}", aboCod, Thread.currentThread().getName());
        List<Map<String, Object>> documentos = obtenerDocumentosPorAbonado(aboCod);
        logger.info("Procesando {} documentos para AboCod: {} en hilo: {}", documentos.size(), aboCod, Thread.currentThread().getName());

        // Procesar cada documento en paralelo
        documentos.forEach(this::ejecutarApliqueProcedimiento);

        // Devolver un CompletableFuture indicando que la tarea ha terminado
        return CompletableFuture.completedFuture(null);

    }

    // Ejecutar el procedimiento almacenado 'Aplique' para cada documento
    @Async("taskExecutor")
    public CompletableFuture<Void> ejecutarApliqueProcedimiento(Map<String, Object> documento) {
        try {
            Integer aboCod = (Integer) documento.get("AboCod");
            Integer docNro = (Integer) documento.get("DocNro");
            String docTpo = (String) documento.get("DocTpo");
            BigDecimal docImpBigDecimal = (BigDecimal) documento.get("DocImp");
            Integer docImp = docImpBigDecimal.intValue();
            String docFch = documento.get("DocFch").toString();

            String sql = "EXEC [dbo].[ApliqueOpt] @pABOCOD = ?, @Documento = ?, @pDocTpo = ?, @pDocImp = ?, @pDocFch = ?";
            jdbcTemplate.update(sql, aboCod, docNro, docTpo, docImp, docFch);

            // insertar en tabla PROCESO_APLICACION
            LocalDateTime fechaProcesamiento = LocalDateTime.now();
            String resultado = "Éxito-" + Thread.currentThread().getName();
            String sqlInsert = "INSERT INTO PROCESO_APLICACION (abonado_id, resultado, fecha_procesamiento) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlInsert, aboCod, resultado, fechaProcesamiento);

            // Log para seguimiento
            logger.info("Aplique ejecutado para AboCod: {}, DocNro: {}, Hilo: {}", aboCod, docNro, Thread.currentThread().getName());
        } catch (Exception e) {
            logger.error("Error ejecutando Aplique para documento: {}", documento, e);
        }

        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> ejecutarApliqueProcedimiento(Map<String, Object> documento, String taskId) {
        try {
            Integer aboCod = (Integer) documento.get("AboCod");
            Integer docNro = (Integer) documento.get("DocNro");
            String docTpo = (String) documento.get("DocTpo");
            BigDecimal docImpBigDecimal = (BigDecimal) documento.get("DocImp");
            Integer docImp = docImpBigDecimal.intValue();
            String docFch = documento.get("DocFch").toString();

            logger.info("Ejecutando Aplique para AboCod: {}, DocNro: {}, Hilo: {}", aboCod, docNro, Thread.currentThread().getName());

            String sql = "EXEC [dbo].[ApliqueOpt] @pABOCOD = ?, @Documento = ?, @pDocTpo = ?, @pDocImp = ?, @pDocFch = ?";
            jdbcTemplate.update(sql, aboCod, docNro, docTpo, docImp, docFch);

            LocalDateTime fechaProcesamiento = LocalDateTime.now();
            String resultado = "Éxito-" + Thread.currentThread().getName();
            String sqlInsert = "INSERT INTO PROCESO_APLICACION (abonado_id, resultado, fecha_procesamiento) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlInsert, aboCod, resultado, fechaProcesamiento);

        } catch (Exception e) {
            logger.error("Error ejecutando Aplique para documento: {}", documento, e);
        }

        return CompletableFuture.completedFuture(null);
    }

    // Obtener documentos de un abonado
    private List<Map<String, Object>> obtenerDocumentosPorAbonado(int aboCod) {
        String sql = "SELECT T1.AboCod, T1.DocNro, T1.DocTpo, T1.DocFch, T1.DocImp " +
                "FROM Document T1 " +
                "INNER JOIN TiposDoc T2 ON T2.DocTpo = T1.DocTpo " +
                "WHERE T1.AboCod = ? AND T2.DocFlgTpo IN ('R', 'N', 'D', 'B') " +
                "ORDER BY T1.AboCod, T1.DocFch, T1.DocTpo, T1.DocNro";
        return jdbcTemplate.queryForList(sql, aboCod);
    }

    private boolean esTareaCancelada(String taskId) {
        String sql = "SELECT task_status FROM TASKS WHERE task_id = ?";
        String estado = jdbcTemplate.queryForObject(sql, new Object[]{taskId}, String.class);
        return "CANCELED".equals(estado);
    }
}

//@Service
//public class ApliqueAsyncService {
//
//    private static final Logger logger = LoggerFactory.getLogger(ApliqueAsyncService.class);
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    // Procesar todos los documentos de un abonado en paralelo
//    @Async("taskExecutor")
//    public CompletableFuture<Void> procesarDocumentosPorAbonado(int aboCod) {
//        List<Map<String, Object>> documentos = obtenerDocumentosPorAbonado(aboCod);
//        logger.info("Procesando {} documentos para AboCod: {} en hilo: {}", documentos.size(), aboCod, Thread.currentThread().getName());
//
//        // Procesar cada documento en paralelo
//        documentos.forEach(this::ejecutarApliqueProcedimiento);
//
//        // Devolver un CompletableFuture indicando que la tarea ha terminado
//        return CompletableFuture.completedFuture(null);
//    }
//
//    // Obtener documentos de un abonado
//    private List<Map<String, Object>> obtenerDocumentosPorAbonado(int aboCod) {
//        String sql = "SELECT T1.AboCod, T1.DocNro, T1.DocTpo, T1.DocFch, T1.DocImp " +
//                "FROM Document T1 " +
//                "INNER JOIN TiposDoc T2 ON T2.DocTpo = T1.DocTpo " +
//                "WHERE T1.AboCod = ? AND T2.DocFlgTpo IN ('R', 'N', 'D', 'B') " +
//                "ORDER BY T1.AboCod, T1.DocFch, T1.DocTpo, T1.DocNro";
//        return jdbcTemplate.queryForList(sql, aboCod);
//    }
//
//    // Ejecutar el procedimiento almacenado 'Aplique' para cada documento
//    @Async("taskExecutor")
//    public CompletableFuture<Void> ejecutarApliqueProcedimiento(Map<String, Object> documento) {
//        try {
//            Integer aboCod = (Integer) documento.get("AboCod");
//            Integer docNro = (Integer) documento.get("DocNro");
//            String docTpo = (String) documento.get("DocTpo");
//            BigDecimal docImpBigDecimal = (BigDecimal) documento.get("DocImp");
//            Integer docImp = docImpBigDecimal.intValue();
//            String docFch = documento.get("DocFch").toString();
//
//            String sql = "EXEC [dbo].[ApliqueOpt] @pABOCOD = ?, @Documento = ?, @pDocTpo = ?, @pDocImp = ?, @pDocFch = ?";
//            jdbcTemplate.update(sql, aboCod, docNro, docTpo, docImp, docFch);
//
//            // insertar en tabla PROCESO_APLICACION
//            LocalDateTime fechaProcesamiento = LocalDateTime.now();
//            String resultado = "Éxito-" + Thread.currentThread().getName();
//            String sqlInsert = "INSERT INTO PROCESO_APLICACION (abonado_id, resultado, fecha_procesamiento) VALUES (?, ?, ?)";
//            jdbcTemplate.update(sqlInsert, aboCod, resultado, fechaProcesamiento);
//
//            // Log para seguimiento
//            logger.info("Aplique ejecutado para AboCod: {}, DocNro: {}, Hilo: {}", aboCod, docNro, Thread.currentThread().getName());
//        } catch (Exception e) {
//            logger.error("Error ejecutando Aplique para documento: {}", documento, e);
//        }
//
//        return CompletableFuture.completedFuture(null);
//    }
//}