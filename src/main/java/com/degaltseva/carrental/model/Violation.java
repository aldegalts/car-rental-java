package com.degaltseva.carrental.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Violation {

    private Long id;
    private Long rentalId;
    private Long violationTypeId;
    private String description;
    private BigDecimal fineAmount;
    private LocalDateTime violationDate;
    private boolean paid;

    // transient display fields
    private String violationTypeName;

    public Violation() {}

    public Violation(Long id, Long rentalId, Long violationTypeId, String description,
                     BigDecimal fineAmount, LocalDateTime violationDate, boolean paid) {
        this.id = id;
        this.rentalId = rentalId;
        this.violationTypeId = violationTypeId;
        this.description = description;
        this.fineAmount = fineAmount;
        this.violationDate = violationDate;
        this.paid = paid;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRentalId() { return rentalId; }
    public void setRentalId(Long rentalId) { this.rentalId = rentalId; }

    public Long getViolationTypeId() { return violationTypeId; }
    public void setViolationTypeId(Long violationTypeId) { this.violationTypeId = violationTypeId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getFineAmount() { return fineAmount; }
    public void setFineAmount(BigDecimal fineAmount) { this.fineAmount = fineAmount; }

    public LocalDateTime getViolationDate() { return violationDate; }
    public void setViolationDate(LocalDateTime violationDate) { this.violationDate = violationDate; }

    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    public String getViolationTypeName() { return violationTypeName; }
    public void setViolationTypeName(String violationTypeName) { this.violationTypeName = violationTypeName; }
}
