package com.degaltseva.carrental.model;

import java.math.BigDecimal;

public class CarCategory {

    private Long id;
    private String categoryName;
    private String description;
    private BigDecimal baseCost;

    public CarCategory() {}

    public CarCategory(Long id, String categoryName, String description, BigDecimal baseCost) {
        this.id = id;
        this.categoryName = categoryName;
        this.description = description;
        this.baseCost = baseCost;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getBaseCost() { return baseCost; }
    public void setBaseCost(BigDecimal baseCost) { this.baseCost = baseCost; }
}
