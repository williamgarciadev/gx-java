package com.example.processserver.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AbonadoDto {
    private int aboCod;
    private String aboNom;
    private String aboApe;
    private String aboDir;
    private String aboTel;
    private String aboEmail;
    private LocalDateTime aboFchIng;
    private String aboSts;
    private String aboGpon;
    private String secNro;
    private String grpAfiDsc;
    private String sectCod;
    private String sectDsc;
    private BigDecimal totalFacturas;
    private BigDecimal totalPagos;
    private BigDecimal saldo;
    private int facturasImpagas;
    private String paqDsc;
    private int proVelCont;

    // Getters y Setters para los nuevos campos

    public String getAboGpon() {
        return aboGpon;
    }

    public void setAboGpon(String aboGpon) {
        this.aboGpon = aboGpon;
    }

    public String getSecNro() {
        return secNro;
    }

    public void setSecNro(String secNro) {
        this.secNro = secNro;
    }

    public String getGrpAfiDsc() {
        return grpAfiDsc;
    }

    public void setGrpAfiDsc(String grpAfiDsc) {
        this.grpAfiDsc = grpAfiDsc;
    }

    public String getSectCod() {
        return sectCod;
    }

    public void setSectCod(String sectCod) {
        this.sectCod = sectCod;
    }

    public String getSectDsc() {
        return sectDsc;
    }

    public void setSectDsc(String sectDsc) {
        this.sectDsc = sectDsc;
    }

    public BigDecimal getTotalFacturas() {
        return totalFacturas;
    }

    public void setTotalFacturas(BigDecimal totalFacturas) {
        this.totalFacturas = totalFacturas;
    }

    public BigDecimal getTotalPagos() {
        return totalPagos;
    }

    public void setTotalPagos(BigDecimal totalPagos) {
        this.totalPagos = totalPagos;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public int getFacturasImpagas() {
        return facturasImpagas;
    }

    public void setFacturasImpagas(int facturasImpagas) {
        this.facturasImpagas = facturasImpagas;
    }

    public String getPaqDsc() {
        return paqDsc;
    }

    public void setPaqDsc(String paqDsc) {
        this.paqDsc = paqDsc;
    }

    public int getProVelCont() {
        return proVelCont;
    }

    public void setProVelCont(int proVelCont) {
        this.proVelCont = proVelCont;
    }

    public int getAboCod() {
        return aboCod;
    }

    public void setAboCod(int aboCod) {
        this.aboCod = aboCod;
    }

    public String getAboNom() {
        return aboNom;
    }

    public void setAboNom(String aboNom) {
        this.aboNom = aboNom;
    }

    public String getAboApe() {
        return aboApe;
    }

    public void setAboApe(String aboApe) {
        this.aboApe = aboApe;
    }

    public String getAboDir() {
        return aboDir;
    }

    public void setAboDir(String aboDir) {
        this.aboDir = aboDir;
    }

    public String getAboTel() {
        return aboTel;
    }

    public void setAboTel(String aboTel) {
        this.aboTel = aboTel;
    }

    public String getAboEmail() {
        return aboEmail;
    }

    public void setAboEmail(String aboEmail) {
        this.aboEmail = aboEmail;
    }

    public LocalDateTime getAboFchIng() {
        return aboFchIng;
    }

    public void setAboFchIng(LocalDateTime aboFchIng) {
        this.aboFchIng = aboFchIng;
    }

    public String getAboSts() {
        return aboSts;
    }

    public void setAboSts(String aboSts) {
        this.aboSts = aboSts;
    }
}
