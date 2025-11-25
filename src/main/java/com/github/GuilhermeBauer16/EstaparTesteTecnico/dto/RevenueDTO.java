package com.github.GuilhermeBauer16.EstaparTesteTecnico.dto;

import java.time.OffsetDateTime;

public class RevenueDTO {


    private String currency;
    private Double amount;
    private OffsetDateTime timestamp;

    public RevenueDTO() {
    }

    public RevenueDTO(String currency, Double amount, OffsetDateTime timestamp) {
        this.currency = currency;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
