package com.example.processserver.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "DOCDET")
public class Docdet {
    @EmbeddedId
    private DocdetId id;

    @Column(name = "DImpApli", precision = 12, scale = 4)
    private BigDecimal dImpApli;

}