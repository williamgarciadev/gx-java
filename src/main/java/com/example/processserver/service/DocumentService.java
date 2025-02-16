package com.example.processserver.service;

import com.example.processserver.model.Document;
import com.example.processserver.model.DocumentId;
import com.example.processserver.repository.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentoRepository documentRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    public Optional<Document> findById(DocumentId id) {
        return documentRepository.findById(id);
    }

    public Document save(Document document) {
        return documentRepository.save(document);
    }

    public void deleteById(DocumentId id) {
        documentRepository.deleteById(id);
    }


    public int processDocuments() {
        String sql = "UPDATE DOCUMENT SET DocSts = 'I', DocImp = 0 WHERE DocSts = 'D' AND (DocTpo = '1' OR DocTpo = '4' OR DocTpo = '6' OR DocTpo = '7')";
        int rowsAffected = jdbcTemplate.update(sql);
        System.out.println("Number of documents processed: " + rowsAffected);
        return rowsAffected;
    }

    public List<Map<String, Object>> obtenerDocumentosPorAbonado(int aboCod) {
        String sql = "SELECT T1.AboCod, T2.DocFlgTpo, T1.DocNro, T1.DocTpo, T1.DocFch, T1.DocImp " +
                "FROM Document T1 " +
                "INNER JOIN TiposDoc T2 ON T2.DocTpo = T1.DocTpo " +
                "WHERE T1.AboCod = ? AND T2.DocFlgTpo IN ('R', 'N', 'D', 'B') " +
                "ORDER BY T1.AboCod, T1.DocFch, T1.DocTpo, T1.DocNro";

        return jdbcTemplate.queryForList(sql, aboCod);
    }
}
