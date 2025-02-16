package com.example.processserver.model;

import jakarta.persistence.*;


@Entity
@Table(name = "TIPOSDOC")
public class TiposDoc {

    @Id
    @Column(name = "DocTpo", nullable = false)
    private char docTpo;

    @Column(name = "DocDsc", length = 15)
    private String docDsc;

    @Column(name = "DocDebHab")
    private Character docDebHab;

    @Column(name = "DocFlgTpo")
    private Character docFlgTpo;






    // Getters and setters


    public char getDocTpo() {
        return docTpo;
    }

    public void setDocTpo(char docTpo) {
        this.docTpo = docTpo;
    }

    public String getDocDsc() {
        return docDsc;
    }

    public void setDocDsc(String docDsc) {
        this.docDsc = docDsc;
    }

    public Character getDocDebHab() {
        return docDebHab;
    }

    public void setDocDebHab(Character docDebHab) {
        this.docDebHab = docDebHab;
    }

    public Character getDocFlgTpo() {
        return docFlgTpo;
    }

    public void setDocFlgTpo(Character docFlgTpo) {
        this.docFlgTpo = docFlgTpo;
    }



}

