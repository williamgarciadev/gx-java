package com.example.processserver.service;

import com.example.processserver.model.Abonado;
import com.example.processserver.repository.AbonadoRepository;
import com.example.processserver.repository.DocumentoRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ExcelExportService {

    @Autowired
    private AbonadoRepository abonadosRepository;

    @Autowired
    private DocumentoRepository documentRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Genera el archivo Excel con los resultados del proceso
    public ByteArrayInputStream generarExcel() throws IOException {
        List<Map<String, Object>> resultados = obtenerResultadosDeProcesos();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Resultados de Aplique");

            // Crear encabezados
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Abonado ID");
            headerRow.createCell(1).setCellValue("Resultado");
            headerRow.createCell(2).setCellValue("Fecha de Procesamiento");

            // Agregar datos
            int rowNum = 1;
            for (Map<String, Object> resultado : resultados) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue((Integer) resultado.get("abonado_id"));
                row.createCell(1).setCellValue((String) resultado.get("resultado"));
                row.createCell(2).setCellValue(resultado.get("fecha_procesamiento").toString());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    // Consulta para obtener los datos insertados en la tabla
    private List<Map<String, Object>> obtenerResultadosDeProcesos() {
        String sql = "SELECT abonado_id, resultado, fecha_procesamiento FROM PROCESO_APLICACION ORDER BY abonado_id";
        return jdbcTemplate.queryForList(sql);
    }

    public void exportDataToExcel(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Abonados");

        List<Abonado> abonadosList = abonadosRepository.findAll();
        int rowCount = 0;

        Row headerRow = sheet.createRow(rowCount++);
        headerRow.createCell(0).setCellValue("AboCod");
        headerRow.createCell(1).setCellValue("AboNom");
        headerRow.createCell(2).setCellValue("AboApe");
        headerRow.createCell(3).setCellValue("AboDir");
        headerRow.createCell(4).setCellValue("AboTel");
        headerRow.createCell(5).setCellValue("AboEmail");
        headerRow.createCell(6).setCellValue("AboFchIng");
        headerRow.createCell(7).setCellValue("AboSts");

        for (Abonado abonado : abonadosList) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(abonado.getAboCod());
            row.createCell(1).setCellValue(abonado.getAboNom());
            row.createCell(2).setCellValue(abonado.getAboApe());
            row.createCell(3).setCellValue(abonado.getAboDir());
            row.createCell(4).setCellValue(abonado.getAboTel());
            row.createCell(5).setCellValue(abonado.getAboEmail());
            row.createCell(6).setCellValue(abonado.getAboFchIng().toString());
            row.createCell(7).setCellValue(abonado.getAboSts());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=abonados.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
