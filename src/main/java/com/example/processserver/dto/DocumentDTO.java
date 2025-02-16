package com.example.processserver.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DocumentDTO {

    private Integer aboCod;
    private Integer docNro;
    private LocalDateTime docFch;
    private String docTpo;
    private String docSts;
    private LocalDateTime docFchPgo;
    private Integer docImp;
    private Integer docImpApl;

    // Getters and Setters


    public Integer getAboCod() {
        return aboCod;
    }

    public void setAboCod(Integer aboCod) {
        this.aboCod = aboCod;
    }

    public Integer getDocNro() {
        return docNro;
    }

    public void setDocNro(Integer docNro) {
        this.docNro = docNro;
    }

    public LocalDateTime getDocFch() {
        return docFch;
    }

    public void setDocFch(LocalDateTime docFch) {
        this.docFch = docFch;
    }

    public String getDocTpo() {
        return docTpo;
    }

    public void setDocTpo(String docTpo) {
        this.docTpo = docTpo;
    }

    public String getDocSts() {
        return docSts;
    }

    public void setDocSts(String docSts) {
        this.docSts = docSts;
    }

    public LocalDateTime getDocFchPgo() {
        return docFchPgo;
    }

    public void setDocFchPgo(LocalDateTime docFchPgo) {
        this.docFchPgo = docFchPgo;
    }

    public Integer getDocImp() {
        return docImp;
    }

    public void setDocImp(Integer docImp) {
        this.docImp = docImp;
    }

    public Integer getDocImpApl() {
        return docImpApl;
    }

    public void setDocImpApl(Integer docImpApl) {
        this.docImpApl = docImpApl;
    }
}
