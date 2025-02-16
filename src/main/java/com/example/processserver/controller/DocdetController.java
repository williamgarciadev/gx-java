package com.example.processserver.controller;

import com.example.processserver.model.Docdet;
import com.example.processserver.model.DocdetId;
import com.example.processserver.service.DocdetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/docdet")
public class DocdetController {

    @Autowired
    private DocdetService docdetService;

    @GetMapping
    public ResponseEntity<List<Docdet>> getAllDocdets() {
        List<Docdet> docdets = docdetService.findAll();
        return new ResponseEntity<>(docdets, HttpStatus.OK);
    }

    @GetMapping("/{aboCod}/{docTpo}/{docTpoDoc}/{docNro}/{docFch}/{docNroDoc}")
    public ResponseEntity<Docdet> getDocdetById(@PathVariable Integer aboCod,
                                                @PathVariable Character docTpo,
                                                @PathVariable Character docTpoDoc,
                                                @PathVariable Integer docNro,
                                                @PathVariable Instant docFch,
                                                @PathVariable Integer docNroDoc) {
        DocdetId id = new DocdetId();
        id.setAboCod(aboCod);
        id.setDocTpo(docTpo);
        id.setDocTpoDoc(docTpoDoc);
        id.setDocNro(docNro);
        id.setDocFch(docFch);
        id.setDocNroDoc(docNroDoc);
        Optional<Docdet> docdet = docdetService.findById(id);
        return docdet.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Docdet> createDocdet(@RequestBody Docdet docdet) {
        Docdet createdDocdet = docdetService.save(docdet);
        return new ResponseEntity<>(createdDocdet, HttpStatus.CREATED);
    }

    @PutMapping("/{aboCod}/{docTpo}/{docTpoDoc}/{docNro}/{docFch}/{docNroDoc}")
    public ResponseEntity<Docdet> updateDocdet(@PathVariable Integer aboCod,
                                               @PathVariable Character docTpo,
                                               @PathVariable Character docTpoDoc,
                                               @PathVariable Integer docNro,
                                               @PathVariable Instant docFch,
                                               @PathVariable Integer docNroDoc,
                                               @RequestBody Docdet docdet) {
        DocdetId id = new DocdetId();
        id.setAboCod(aboCod);
        id.setDocTpo(docTpo);
        id.setDocTpoDoc(docTpoDoc);
        id.setDocNro(docNro);
        id.setDocFch(docFch);
        id.setDocNroDoc(docNroDoc);
        if (!docdetService.findById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        docdet.setId(id);
        Docdet updatedDocdet = docdetService.save(docdet);
        return new ResponseEntity<>(updatedDocdet, HttpStatus.OK);
    }

    @DeleteMapping("/{aboCod}/{docTpo}/{docTpoDoc}/{docNro}/{docFch}/{docNroDoc}")
    public ResponseEntity<Void> deleteDocdet(@PathVariable Integer aboCod,
                                             @PathVariable Character docTpo,
                                             @PathVariable Character docTpoDoc,
                                             @PathVariable Integer docNro,
                                             @PathVariable Instant docFch,
                                             @PathVariable Integer docNroDoc) {
        DocdetId id = new DocdetId();
        id.setAboCod(aboCod);
        id.setDocTpo(docTpo);
        id.setDocTpoDoc(docTpoDoc);
        id.setDocNro(docNro);
        id.setDocFch(docFch);
        id.setDocNroDoc(docNroDoc);
        if (!docdetService.findById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        docdetService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Borrar todos los registros
    @DeleteMapping
    public ResponseEntity<Void> deleteAllDocdets() {
        docdetService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
