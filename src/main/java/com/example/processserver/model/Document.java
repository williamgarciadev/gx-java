package com.example.processserver.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@IdClass(DocumentId.class)
@Table(name = "DOCUMENT")
public class Document {

    @Id
    @Column(name = "AboCod", nullable = false)
    private int aboCod;

    @Id
    @Column(name = "DocNro", nullable = false)
    private int docNro;

    @Id
    @Column(name = "DocFch", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date docFch;

    @Id
    @Column(name = "DocTpo", nullable = false)
    private char docTpo;

    @Column(name = "DocRef", length = 10)
    private String docRef;

    @Column(name = "DocSts")
    private Character docSts;

    @Column(name = "DocFlgImp")
    private Character docFlgImp;

    @Column(name = "DocDscPag", length = 30)
    private String docDscPag;

    @Column(name = "CobCod", length = 2)
    private String cobCod;

    @Column(name = "OrgCobCod")
    private Character orgCobCod;

    @Column(name = "DocImp")
    private BigDecimal docImp;

    @Column(name = "DocUsr", length = 10)
    private String docUsr;

    public int getAboCod() {
        return aboCod;
    }

    public void setAboCod(int aboCod) {
        this.aboCod = aboCod;
    }

    public int getDocNro() {
        return docNro;
    }

    public void setDocNro(int docNro) {
        this.docNro = docNro;
    }

    public Date getDocFch() {
        return docFch;
    }

    public void setDocFch(Date docFch) {
        this.docFch = docFch;
    }

    public char getDocTpo() {
        return docTpo;
    }

    public void setDocTpo(char docTpo) {
        this.docTpo = docTpo;
    }

    public String getDocRef() {
        return docRef;
    }

    public void setDocRef(String docRef) {
        this.docRef = docRef;
    }

    public Character getDocSts() {
        return docSts;
    }

    public void setDocSts(Character docSts) {
        this.docSts = docSts;
    }

    public Character getDocFlgImp() {
        return docFlgImp;
    }

    public void setDocFlgImp(Character docFlgImp) {
        this.docFlgImp = docFlgImp;
    }

    public String getDocDscPag() {
        return docDscPag;
    }

    public void setDocDscPag(String docDscPag) {
        this.docDscPag = docDscPag;
    }

    public String getCobCod() {
        return cobCod;
    }

    public void setCobCod(String cobCod) {
        this.cobCod = cobCod;
    }

    public Character getOrgCobCod() {
        return orgCobCod;
    }

    public void setOrgCobCod(Character orgCobCod) {
        this.orgCobCod = orgCobCod;
    }

    public BigDecimal getDocImp() {
        return docImp;
    }

    public void setDocImp(BigDecimal docImp) {
        this.docImp = docImp;
    }

    public String getDocUsr() {
        return docUsr;
    }

    public void setDocUsr(String docUsr) {
        this.docUsr = docUsr;
    }
}
