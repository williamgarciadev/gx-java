package com.example.processserver.controller;

import com.example.processserver.model.Document;
import com.example.processserver.model.DocumentId;
import com.example.processserver.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> documents = documentService.findAll();
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("/{aboCod}/{docNro}/{docFch}/{docTpo}")
    public ResponseEntity<Document> getDocumentById(@PathVariable int aboCod,
                                                    @PathVariable int docNro,
                                                    @PathVariable Date docFch,
                                                    @PathVariable char docTpo) {
        DocumentId id = new DocumentId(aboCod, docNro, docFch, docTpo);
        Optional<Document> document = documentService.findById(id);
        return document.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Document> createDocument(@RequestBody Document document) {
        Document createdDocument = documentService.save(document);
        return new ResponseEntity<>(createdDocument, HttpStatus.CREATED);
    }

    @PutMapping("/{aboCod}/{docNro}/{docFch}/{docTpo}")
    public ResponseEntity<Document> updateDocument(@PathVariable int aboCod,
                                                   @PathVariable int docNro,
                                                   @PathVariable Date docFch,
                                                   @PathVariable char docTpo,
                                                   @RequestBody Document document) {
        DocumentId id = new DocumentId(aboCod, docNro, docFch, docTpo);
        if (!documentService.findById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        document.setAboCod(aboCod);
        document.setDocNro(docNro);
        document.setDocFch(docFch);
        document.setDocTpo(docTpo);
        Document updatedDocument = documentService.save(document);
        return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
    }

    @DeleteMapping("/{aboCod}/{docNro}/{docFch}/{docTpo}")
    public ResponseEntity<Void> deleteDocument(@PathVariable int aboCod,
                                               @PathVariable int docNro,
                                               @PathVariable Date docFch,
                                               @PathVariable char docTpo) {
        DocumentId id = new DocumentId(aboCod, docNro, docFch, docTpo);
        if (!documentService.findById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        documentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/process")
    public ResponseEntity<String> processDocuments() {
        int processed = documentService.processDocuments();
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
