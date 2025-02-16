package com.example.processserver.service;

import com.example.processserver.model.Docdet;
import com.example.processserver.model.DocdetId;
import com.example.processserver.repository.DocDetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocdetService {

    @Autowired
    private DocDetRepository docdetRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Docdet> findAll() {
        return docdetRepository.findAll();
    }

    public Optional<Docdet> findById(DocdetId id) {
        return docdetRepository.findById(id);
    }

    public Docdet save(Docdet docdet) {
        return docdetRepository.save(docdet);
    }

    public void deleteById(DocdetId id) {
        docdetRepository.deleteById(id);
    }

    public void deleteAll() {
        String deleteSql = "DELETE FROM DOCDET";
        jdbcTemplate.update(deleteSql);
    }
}
