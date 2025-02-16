package com.example.processserver.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class DocdetId implements java.io.Serializable {
    private static final long serialVersionUID = 2381534650357543002L;
    @NotNull
    @Column(name = "AboCod", nullable = false)
    private Integer aboCod;

    @NotNull
    @Column(name = "DocNro", nullable = false)
    private Integer docNro;

    @NotNull
    @Column(name = "DocFch", nullable = false)
    private Instant docFch;

    @NotNull
    @Column(name = "DocTpo", nullable = false)
    private Character docTpo;

    @NotNull
    @Column(name = "DocNroDoc", nullable = false)
    private Integer docNroDoc;

    @NotNull
    @Column(name = "DocTpoDoc", nullable = false)
    private Character docTpoDoc;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DocdetId entity = (DocdetId) o;
        return Objects.equals(this.aboCod, entity.aboCod) &&
                Objects.equals(this.docTpo, entity.docTpo) &&
                Objects.equals(this.docTpoDoc, entity.docTpoDoc) &&
                Objects.equals(this.docNro, entity.docNro) &&
                Objects.equals(this.docFch, entity.docFch) &&
                Objects.equals(this.docNroDoc, entity.docNroDoc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aboCod, docTpo, docTpoDoc, docNro, docFch, docNroDoc);
    }

}