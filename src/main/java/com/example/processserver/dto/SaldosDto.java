package com.example.processserver.dto;

import java.math.BigDecimal;



public class SaldosDto {

    private BigDecimal totalFacturas;
    private BigDecimal totalPagos;

    public SaldosDto() {
        this.totalFacturas = BigDecimal.ZERO;
        this.totalPagos = BigDecimal.ZERO;
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
        return totalFacturas.subtract(totalPagos);
    }
}

