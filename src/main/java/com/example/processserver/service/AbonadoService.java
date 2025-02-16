package com.example.processserver.service;

import com.example.processserver.dto.AbonadoDto;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AbonadoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CuentaApliqueService cuentaApliqueService;

    // Metodo para listar todos los abonados

    public List<AbonadoDto> listarAbonados() {
        String sql = "SELECT * FROM ABONADOS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return mapRowToAbonadosDto(rs);
        });
    }

    private AbonadoDto mapRowToAbonadosDto(ResultSet rs) throws SQLException {
        AbonadoDto abonado = new AbonadoDto();
        abonado.setAboCod(rs.getInt("AboCod"));
        abonado.setAboNom(convertToUTF8(rs.getString("AboNom")));
        abonado.setAboApe(convertToUTF8(rs.getString("AboApe")));
        abonado.setAboDir(convertToUTF8(rs.getString("AboDir")));

        return abonado;


    }

    // Encabezados de las columnas del archivo Excel
    private static final String[] HEADERS = {
            "AboCod", "AboNom", "AboApe", "AboDir", "AboTel", "AboEmail", "AboFchIng", "AboSts",
            "Total Facturas", "Total Pagos", "Saldo", "AboGpon", "SecNro", "GrpAfiDsc", "SectCod",
            "SectDsc", "PaqDsc", "ProVelCont", "facturas_impagas"
    };

//    @Async("taskExecutor")
//    public CompletableFuture<Void> procesarAbonados() {
//        List<Integer> abonados = obtenerAbonados(); // Obtiene los IDs de los abonados
//        int numeroDeHilos = 5;  // Ajusta según tu entorno
//
//        int tamanioPorTarea = abonados.size() / numeroDeHilos;
//        System.out.println("tamanioPorTarea = " + tamanioPorTarea);
//
//        for (int i = 0; i < numeroDeHilos; i++) {
//            int desde = i * tamanioPorTarea;
//            int hasta = (i == numeroDeHilos - 1) ? abonados.size() : (i + 1) * tamanioPorTarea;
//
//            // Imprimir los índices de la sublista
//            System.out.println("desde = " + desde);
//            System.out.println("hasta = " + hasta);
//
//            List<Integer> subListaAbonados = abonados.subList(desde, hasta);
//
//            // Procesar la lista de abonados en paralelo
//            subListaAbonados.forEach(abonadoId -> {
//                procesarApliqueDeCuenta(abonadoId);
//            });
//        }
//
//        return CompletableFuture.completedFuture(null);
//    }

    @Async("taskExecutor")  // Ejecuta este método de forma asíncrona en múltiples hilos
    public CompletableFuture<Void> procesarAbonados() {
        List<Integer> abonados = obtenerAbonados();

        for (Integer abonadoId : abonados) {
            // Llama al método procesarApliqueDeCuenta de forma asíncrona
            cuentaApliqueService.procesarApliqueDeCuenta(abonadoId);
        }

        return CompletableFuture.completedFuture(null);
    }

    private List<Integer> obtenerAbonados() {
        String sql = "SELECT AboCod FROM ABONADOS";
        return jdbcTemplate.queryForList(sql, Integer.class);
    }

    // Procesa el aplique de cuenta para un abonado
    public void procesarApliqueDeCuenta(int abonadoId) {
        System.out.println("Iniciando aplique de cuenta para el abonado: " + abonadoId + " en hilo " + Thread.currentThread().getName());
        cuentaApliqueService.procesarApliqueDeCuenta(abonadoId);
        System.out.println("Finalizando aplique de cuenta para el abonado: " + abonadoId);
    }

    @Async("taskExecutor")
    public CompletableFuture<ByteArrayInputStream> generarExcel() {
        try {
            List<AbonadoDto> abonados = getAbonadosData(); // Obtén los datos de la BD
            return CompletableFuture.completedFuture(generateExcelInternal(abonados)); // Genera el Excel
        } catch (IOException e) {
            e.printStackTrace();
            return CompletableFuture.failedFuture(e);
        }
    }

    // Obtención de datos desde la base de datos
    private List<AbonadoDto> getAbonadosData() {
        String sql = "WITH AbonadoSaldos AS (" +
                "    SELECT a.AboCod, a.AboNom, a.AboApe, a.AboDir, a.AboTel, a.AboEmail, a.AboFchIng, a.AboSts, a.AboGpon, " +
                "           a.SecNro, a.SectCod, a.GrpAfiCod, saldos.total_facturas, saldos.total_pagos, saldos.saldo, saldos.facturas_impagas " +
                "    FROM ABONADOS a " +
                "    LEFT JOIN (" +
                "        SELECT d.AboCod, SUM(CASE WHEN t.DocDebHab = 'D' THEN d.DocImp ELSE 0 END) AS total_facturas, " +
                "               SUM(CASE WHEN t.DocDebHab = 'H' THEN d.DocImp ELSE 0 END) AS total_pagos, " +
                "               SUM(CASE WHEN t.DocDebHab = 'D' THEN d.DocImp ELSE 0 END) - " +
                "               SUM(CASE WHEN t.DocDebHab = 'H' THEN d.DocImp ELSE 0 END) AS saldo, " +
                "               COUNT(CASE WHEN d.DocSts = 'I' THEN 1 ELSE NULL END) AS facturas_impagas " +
                "        FROM DOCUMENT d " +
                "        JOIN TIPOSDOC t ON d.DocTpo = t.DocTpo " +
                "        GROUP BY d.AboCod " +
                "    ) AS saldos ON a.AboCod = saldos.AboCod " +
                "), " +
                "Paquetes AS (" +
                "    SELECT c.AboCod, p.PaqDsc, pr.ProVelCont, " +
                "           ROW_NUMBER() OVER (PARTITION BY c.AboCod ORDER BY " +
                "               CASE WHEN p.PaqCod = 1 THEN 0 WHEN p.PaqCod = 3 THEN 1 ELSE 2 END) AS PaqOrder " +
                "    FROM CONTRATO c " +
                "    LEFT JOIN PAQUETE p ON c.PaqCod = p.PaqCod " +
                "    LEFT JOIN PROMOCIO pr ON c.ProCodPr = pr.ProCodPr " +
                "    WHERE c.ConSts <> 'X' " +
                "), " +
                "OrderedPaquetes AS (" +
                "    SELECT AboCod, STRING_AGG(TRIM(PaqDsc), ', ') AS PaqDsc, MAX(ProVelCont) AS ProVelCont " +
                "    FROM Paquetes " +
                "    GROUP BY AboCod " +
                ") " +
                "SELECT a.AboCod, a.AboNom, a.AboApe, a.AboDir, a.AboTel, ISNULL(a.AboEmail, '') AS AboEmail, a.AboFchIng, a.AboSts, " +
                "       ISNULL(a.AboGpon, '') AS AboGpon, a.SecNro, Z.GrpAfiDsc, a.SectCod, S.SectDsc, ISNULL(a.total_facturas, 0) AS total_facturas, " +
                "       ISNULL(a.total_pagos, 0) AS total_pagos, ISNULL(a.saldo, 0) AS Saldo, ISNULL(a.facturas_impagas, 0) AS facturas_impagas, " +
                "       ISNULL(p.PaqDsc, '') AS PaqDsc, ISNULL(p.ProVelCont, 0) AS ProVelCont " +
                "FROM AbonadoSaldos a " +
                "LEFT JOIN OrderedPaquetes p ON a.AboCod = p.AboCod " +
                "LEFT JOIN ABOGRP Z ON A.GrpAfiCod = Z.GrpAfiCod " +
                "LEFT JOIN SECTOR S ON A.SectCod = S.SectCod " +
                "ORDER BY a.AboCod;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            try {
                return mapRowToAbonadoDto(rs);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // Generación del archivo Excel basado en los datos
    private ByteArrayInputStream generateExcelInternal(List<AbonadoDto> abonados) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Abonados");

            createHeaderRow(sheet); // Crea la fila de encabezado

            int rowNum = 1;
            for (AbonadoDto abonado : abonados) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(abonado.getAboCod());
                row.createCell(1).setCellValue(convertToUTF8(abonado.getAboNom()));
                row.createCell(2).setCellValue(convertToUTF8(abonado.getAboApe()));
                row.createCell(3).setCellValue(convertToUTF8(abonado.getAboDir()));
                row.createCell(4).setCellValue(convertToUTF8(abonado.getAboTel()));
                row.createCell(5).setCellValue(convertToUTF8(abonado.getAboEmail()));
                row.createCell(6).setCellValue(abonado.getAboFchIng() != null ? abonado.getAboFchIng().toString() : "");
                row.createCell(7).setCellValue(convertToUTF8(abonado.getAboSts()));
                row.createCell(8).setCellValue(abonado.getTotalFacturas().doubleValue());
                row.createCell(9).setCellValue(abonado.getTotalPagos().doubleValue());
                row.createCell(10).setCellValue(abonado.getSaldo().doubleValue());
                row.createCell(11).setCellValue(convertToUTF8(abonado.getAboGpon()));
                row.createCell(12).setCellValue(abonado.getSecNro());
                row.createCell(13).setCellValue(convertToUTF8(abonado.getGrpAfiDsc()));
                row.createCell(14).setCellValue(convertToUTF8(abonado.getSectCod()));
                row.createCell(15).setCellValue(convertToUTF8(abonado.getSectDsc()));
                row.createCell(16).setCellValue(convertToUTF8(abonado.getPaqDsc()));
                row.createCell(17).setCellValue(abonado.getProVelCont());
                row.createCell(18).setCellValue(abonado.getFacturasImpagas());
            }

            // Escribir el workbook a un flujo de salida y devolverlo como ByteArrayInputStream
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    // Crea la fila de encabezado en el archivo Excel
    private void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < HEADERS.length; i++) {
            headerRow.createCell(i).setCellValue(HEADERS[i]);
        }
    }

    // Mapea los resultados de la consulta SQL al objeto AbonadoDto
    private AbonadoDto mapRowToAbonadoDto(ResultSet rs) throws SQLException, UnsupportedEncodingException {
        AbonadoDto abonado = new AbonadoDto();
        abonado.setAboCod(rs.getInt("AboCod"));
        abonado.setAboNom(convertToUTF8(rs.getString("AboNom")));
        abonado.setAboApe(convertToUTF8(rs.getString("AboApe")));
        abonado.setAboDir(convertToUTF8(rs.getString("AboDir")));
        abonado.setAboTel(rs.getString("AboTel"));
        abonado.setAboEmail(rs.getString("AboEmail"));
        abonado.setAboFchIng(rs.getObject("AboFchIng", LocalDateTime.class));
        abonado.setAboSts(rs.getString("AboSts"));
        abonado.setAboGpon(rs.getString("AboGpon"));
        abonado.setSecNro(rs.getString("SecNro"));
        abonado.setGrpAfiDsc(rs.getString("GrpAfiDsc"));
        abonado.setSectCod(rs.getString("SectCod"));
        abonado.setSectDsc(rs.getString("SectDsc"));
        abonado.setTotalFacturas(rs.getBigDecimal("total_facturas"));
        abonado.setTotalPagos(rs.getBigDecimal("total_pagos"));
        abonado.setSaldo(rs.getBigDecimal("saldo"));
        abonado.setFacturasImpagas(rs.getInt("facturas_impagas"));
        abonado.setPaqDsc(rs.getString("PaqDsc"));
        abonado.setProVelCont(rs.getInt("ProVelCont"));
        return abonado;
    }

    // Convierte el texto a formato UTF-8
    private String convertToUTF8(String input) {
        if (input == null) {
            return "";
        }
        try {
            return new String(input.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return input;
        }
    }
}

//
//import com.example.processserver.dto.AbonadoDto;
//import com.example.processserver.dto.DocumentDTO;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//@Service
//public class AbonadoService {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Async("taskExecutor")
//    public ByteArrayInputStream generarExcelDos() throws IOException {
//        String sql = "select abocod, docnro, docfch, doctpo,docsts,DocFchPgo,CONVERT(INT, ROUND(docimp,0,1)) as docimp, CONVERT(INT, ROUND(DocImpApl,0,1)) as docimpapl" +
//                " from DOCUMENT";
//
//        List<DocumentDTO> documentDTOList = jdbcTemplate.query(sql, (rs, rowNum) -> {
//            return mapRowToDocumentDTO(rs);
//        });
//
//        try (Workbook workbook = new XSSFWorkbook()) {
//            Sheet sheet = workbook.createSheet("DOCUMENTOS");
//
//            Row headerRow = sheet.createRow(0);
//            headerRow.createCell(0).setCellValue("AboCod");
//            headerRow.createCell(1).setCellValue("DocNro");
//            headerRow.createCell(2).setCellValue("DocFch");
//            headerRow.createCell(3).setCellValue("DocTpo");
//            headerRow.createCell(4).setCellValue("DocSts");
//            headerRow.createCell(5).setCellValue("DocFchPgo");
//            headerRow.createCell(6).setCellValue("DocImp");
//            headerRow.createCell(7).setCellValue("DocImpApl");
//
//
//            int rowNum = 1;
//            for (DocumentDTO documentDTO : documentDTOList) {
//                Row row = sheet.createRow(rowNum++);
//                row.createCell(0).setCellValue(documentDTO.getAboCod());
//                row.createCell(1).setCellValue(documentDTO.getDocNro());
//                row.createCell(2).setCellValue(documentDTO.getDocFch().toString());
//                row.createCell(3).setCellValue(documentDTO.getDocTpo());
//                row.createCell(4).setCellValue(documentDTO.getDocSts());
//                row.createCell(5).setCellValue(
//                        documentDTO.getDocFchPgo() != null ? documentDTO.getDocFchPgo().toString() : ""
//                );
//                row.createCell(6).setCellValue(documentDTO.getDocImp().doubleValue());
//                row.createCell(7).setCellValue(documentDTO.getDocImpApl().doubleValue());
//            }
//
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            workbook.write(out);
//            return new ByteArrayInputStream(out.toByteArray());
//        }
//    }
//
//    private DocumentDTO mapRowToDocumentDTO(ResultSet rs) throws SQLException {
//        DocumentDTO documentDTO = new DocumentDTO();
//        documentDTO.setAboCod(rs.getInt("AboCod"));
//        documentDTO.setDocNro(rs.getInt("DocNro"));
//        documentDTO.setDocFch(rs.getObject("DocFch", LocalDateTime.class));
//        documentDTO.setDocTpo(rs.getString("DocTpo"));
//        documentDTO.setDocSts(rs.getString("DocSts"));
//        documentDTO.setDocFchPgo(rs.getObject("DocFchPgo", LocalDateTime.class));
//        documentDTO.setDocImp(rs.getInt("DocImp"));
//        documentDTO.setDocImpApl(rs.getInt("DocImpApl"));
//
//        return documentDTO;
//
//    }
//
//
//    @Async("taskExecutor")
//    public CompletableFuture<ByteArrayInputStream> generarExcel() throws IOException {
//        String sql = "WITH AbonadoSaldos AS (" +
//                "    SELECT a.AboCod, a.AboNom, a.AboApe, a.AboDir, a.AboTel, a.AboEmail, a.AboFchIng, a.AboSts, a.AboGpon, " +
//                "           a.SecNro, a.SectCod, a.GrpAfiCod, saldos.total_facturas, saldos.total_pagos, saldos.saldo, saldos.facturas_impagas " +
//                "    FROM ABONADOS a " +
//                "    LEFT JOIN (" +
//                "        SELECT d.AboCod, SUM(CASE WHEN t.DocDebHab = 'D' THEN d.DocImp ELSE 0 END) AS total_facturas, " +
//                "               SUM(CASE WHEN t.DocDebHab = 'H' THEN d.DocImp ELSE 0 END) AS total_pagos, " +
//                "               SUM(CASE WHEN t.DocDebHab = 'D' THEN d.DocImp ELSE 0 END) - " +
//                "               SUM(CASE WHEN t.DocDebHab = 'H' THEN d.DocImp ELSE 0 END) AS saldo, " +
//                "               COUNT(CASE WHEN d.DocSts = 'I' THEN 1 ELSE NULL END) AS facturas_impagas " +
//                "        FROM DOCUMENT d " +
//                "        JOIN TIPOSDOC t ON d.DocTpo = t.DocTpo " +
//                "        GROUP BY d.AboCod " +
//                "    ) AS saldos ON a.AboCod = saldos.AboCod " +
//                "), " +
//                "Paquetes AS (" +
//                "    SELECT c.AboCod, p.PaqDsc, pr.ProVelCont, " +
//                "           ROW_NUMBER() OVER (PARTITION BY c.AboCod ORDER BY " +
//                "               CASE WHEN p.PaqCod = 1 THEN 0 WHEN p.PaqCod = 3 THEN 1 ELSE 2 END) AS PaqOrder " +
//                "    FROM CONTRATO c " +
//                "    LEFT JOIN PAQUETE p ON c.PaqCod = p.PaqCod " +
//                "    LEFT JOIN PROMOCIO pr ON c.ProCodPr = pr.ProCodPr " +
//                "    WHERE c.ConSts <> 'X' " +
//                "), " +
//                "OrderedPaquetes AS (" +
//                "    SELECT AboCod, STRING_AGG(TRIM(PaqDsc), ', ') AS PaqDsc, MAX(ProVelCont) AS ProVelCont " +
//                "    FROM Paquetes " +
//                "    GROUP BY AboCod " +
//                ") " +
//                "SELECT a.AboCod, a.AboNom, a.AboApe, a.AboDir, a.AboTel, ISNULL(a.AboEmail, '') AS AboEmail, a.AboFchIng, a.AboSts, " +
//                "       ISNULL(a.AboGpon, '') AS AboGpon, a.SecNro, Z.GrpAfiDsc, a.SectCod, S.SectDsc, ISNULL(a.total_facturas, 0) AS total_facturas, " +
//                "       ISNULL(a.total_pagos, 0) AS total_pagos, ISNULL(a.saldo, 0) AS Saldo, ISNULL(a.facturas_impagas, 0) AS facturas_impagas, " +
//                "       ISNULL(p.PaqDsc, '') AS PaqDsc, ISNULL(p.ProVelCont, 0) AS ProVelCont " +
//                "FROM AbonadoSaldos a " +
//                "LEFT JOIN OrderedPaquetes p ON a.AboCod = p.AboCod " +
//                "LEFT JOIN ABOGRP Z ON A.GrpAfiCod = Z.GrpAfiCod " +
//                "LEFT JOIN SECTOR S ON A.SectCod = S.SectCod " +
//                "ORDER BY a.AboCod;";
//
//        List<AbonadoDto> abonados = jdbcTemplate.query(sql, (rs, rowNum) -> {
//            try {
//                return mapRowToAbonadoDto(rs);
//            } catch (UnsupportedEncodingException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//        try (Workbook workbook = new XSSFWorkbook()) {
//            Sheet sheet = workbook.createSheet("Abonados");
//
//            Row headerRow = sheet.createRow(0);
//            headerRow.createCell(0).setCellValue("AboCod");
//            headerRow.createCell(1).setCellValue("AboNom");
//            headerRow.createCell(2).setCellValue("AboApe");
//            headerRow.createCell(3).setCellValue("AboDir");
//            headerRow.createCell(4).setCellValue("AboTel");
//            headerRow.createCell(5).setCellValue("AboEmail");
//            headerRow.createCell(6).setCellValue("AboFchIng");
//            headerRow.createCell(7).setCellValue("AboSts");
//            headerRow.createCell(8).setCellValue("Total Facturas");
//            headerRow.createCell(9).setCellValue("Total Pagos");
//            headerRow.createCell(10).setCellValue("Saldo");
//            headerRow.createCell(11).setCellValue("AboGpon");
//            headerRow.createCell(12).setCellValue("SecNro");
//            headerRow.createCell(13).setCellValue("GrpAfiDsc");
//            headerRow.createCell(14).setCellValue("SectCod");
//            headerRow.createCell(15).setCellValue("SectDsc");
//            headerRow.createCell(16).setCellValue("PaqDsc");
//            headerRow.createCell(17).setCellValue("ProVelCont");
//            headerRow.createCell(18).setCellValue("facturas_impagas");
//
//            int rowNum = 1;
//            for (AbonadoDto abonado : abonados) {
//                Row row = sheet.createRow(rowNum++);
//                row.createCell(0).setCellValue(abonado.getAboCod());
//                row.createCell(1).setCellValue(convertToUTF8(abonado.getAboNom()));
//                row.createCell(2).setCellValue(convertToUTF8(abonado.getAboApe()));
//                row.createCell(3).setCellValue(convertToUTF8(abonado.getAboDir()));
//                row.createCell(4).setCellValue(convertToUTF8(abonado.getAboTel()));
//                row.createCell(5).setCellValue(convertToUTF8(abonado.getAboEmail()));
//                row.createCell(6).setCellValue(abonado.getAboFchIng() != null ? abonado.getAboFchIng().toString() : "");
//                row.createCell(7).setCellValue(convertToUTF8(abonado.getAboSts()));
//                row.createCell(8).setCellValue(abonado.getTotalFacturas().doubleValue());
//                row.createCell(9).setCellValue(abonado.getTotalPagos().doubleValue());
//                row.createCell(10).setCellValue(abonado.getSaldo().doubleValue());
//                row.createCell(11).setCellValue(convertToUTF8(abonado.getAboGpon()));
//                row.createCell(12).setCellValue(abonado.getSecNro());
//                row.createCell(13).setCellValue(convertToUTF8(abonado.getGrpAfiDsc()));
//                row.createCell(14).setCellValue(convertToUTF8(abonado.getSectCod()));
//                row.createCell(15).setCellValue(convertToUTF8(abonado.getSectDsc()));
//                row.createCell(16).setCellValue(convertToUTF8(abonado.getPaqDsc()));
//                row.createCell(17).setCellValue(abonado.getProVelCont());
//                row.createCell(18).setCellValue(abonado.getFacturasImpagas());
//            }
//
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            workbook.write(out);
//            return new ByteArrayInputStream(out.toByteArray());
//        }
//    }
//
//    private AbonadoDto mapRowToAbonadoDto(ResultSet rs) throws SQLException, UnsupportedEncodingException {
//        AbonadoDto abonado = new AbonadoDto();
//        abonado.setAboCod(rs.getInt("AboCod"));
//        abonado.setAboNom(convertToUTF8(rs.getString("AboNom")));
//        abonado.setAboApe(convertToUTF8(rs.getString("AboApe")));
//        abonado.setAboDir(convertToUTF8(rs.getString("AboDir")));
//        abonado.setAboTel(rs.getString("AboTel"));
//        abonado.setAboEmail(rs.getString("AboEmail"));
//        abonado.setAboFchIng(rs.getObject("AboFchIng", LocalDateTime.class));
//        abonado.setAboSts(rs.getString("AboSts"));
//        abonado.setAboGpon(rs.getString("AboGpon"));
//        abonado.setSecNro(rs.getString("SecNro"));
//        abonado.setGrpAfiDsc(rs.getString("GrpAfiDsc"));
//        abonado.setSectCod(rs.getString("SectCod"));
//        abonado.setSectDsc(rs.getString("SectDsc"));
//        abonado.setTotalFacturas(rs.getBigDecimal("total_facturas"));
//        abonado.setTotalPagos(rs.getBigDecimal("total_pagos"));
//        abonado.setSaldo(rs.getBigDecimal("saldo"));
//        abonado.setFacturasImpagas(rs.getInt("facturas_impagas"));
//        abonado.setPaqDsc(rs.getString("PaqDsc"));
//        abonado.setProVelCont(rs.getInt("ProVelCont"));
//
//        return abonado;
//    }
//
//    private String convertToUTF8(String input) {
//        if (input == null) {
//            return null;
//        }
//        try {
//            byte[] bytes = input.getBytes("ISO-8859-1");
//            return new String(bytes, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            return input;
//        }
//    }
//}
