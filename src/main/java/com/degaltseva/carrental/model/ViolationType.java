package com.degaltseva.carrental.model;

import java.math.BigDecimal;

public class ViolationType {

    private Long id;
    private String typeName;
    private BigDecimal defaultFine;
    private String description;

    public ViolationType() {}

    public ViolationType(Long id, String typeName, BigDecimal defaultFine, String description) {
        this.id = id;
        this.typeName = typeName;
        this.defaultFine = defaultFine;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public BigDecimal getDefaultFine() { return defaultFine; }
    public void setDefaultFine(BigDecimal defaultFine) { this.defaultFine = defaultFine; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
