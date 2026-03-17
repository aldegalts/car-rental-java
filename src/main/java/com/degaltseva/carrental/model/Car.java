package com.degaltseva.carrental.model;

import java.math.BigDecimal;

public class Car {

    private Long id;
    private String brand;
    private String model;
    private int year;
    private Long categoryId;
    private String licensePlate;
    private Long colorId;
    private BigDecimal dailyCost;
    private Long carStatusId;

    // Для отображения
    private String categoryName;
    private String colorName;
    private String colorHex;
    private String statusName;

    public Car() {}

    public Car(Long id, String brand, String model, int year, Long categoryId,
               String licensePlate, Long colorId, BigDecimal dailyCost, Long carStatusId) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.categoryId = categoryId;
        this.licensePlate = licensePlate;
        this.colorId = colorId;
        this.dailyCost = dailyCost;
        this.carStatusId = carStatusId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public Long getColorId() { return colorId; }
    public void setColorId(Long colorId) { this.colorId = colorId; }

    public BigDecimal getDailyCost() { return dailyCost; }
    public void setDailyCost(BigDecimal dailyCost) { this.dailyCost = dailyCost; }

    public Long getCarStatusId() { return carStatusId; }
    public void setCarStatusId(Long carStatusId) { this.carStatusId = carStatusId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getColorName() { return colorName; }
    public void setColorName(String colorName) { this.colorName = colorName; }

    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }

    public String getStatusName() { return statusName; }
    public void setStatusName(String statusName) { this.statusName = statusName; }
}
