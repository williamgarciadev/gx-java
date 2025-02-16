package com.example.processserver.model;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class DocumentId implements Serializable {
    private int aboCod;
    private int docNro;
    private Date docFch;
    private char docTpo;

    // Default constructor
    public DocumentId() {
    }

    // Constructor con parámetros
    public DocumentId(int aboCod, int docNro, Date docFch, char docTpo) {
        this.aboCod = aboCod;
        this.docNro = docNro;
        this.docFch = docFch;
        this.docTpo = docTpo;
    }

    // Getters y setters
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

    // hashCode y equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentId that = (DocumentId) o;
        return aboCod == that.aboCod && docNro == that.docNro && docTpo == that.docTpo && Objects.equals(docFch, that.docFch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aboCod, docNro, docFch, docTpo);
    }
}
