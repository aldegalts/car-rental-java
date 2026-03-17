package com.degaltseva.carrental.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Rental {

    private Long id;
    private Long clientId;
    private Long carId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal totalAmount;
    private Long rentalStatusId;

    // transient display fields
    private String clientName;
    private String carName;
    private String carLicensePlate;
    private String statusName;

    public Rental() {}

    public Rental(Long id, Long clientId, Long carId, LocalDateTime startDate,
                  LocalDateTime endDate, BigDecimal totalAmount, Long rentalStatusId) {
        this.id = id;
        this.clientId = clientId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalAmount = totalAmount;
        this.rentalStatusId = rentalStatusId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public Long getRentalStatusId() { return rentalStatusId; }
    public void setRentalStatusId(Long rentalStatusId) { this.rentalStatusId = rentalStatusId; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getCarName() { return carName; }
    public void setCarName(String carName) { this.carName = carName; }

    public String getCarLicensePlate() { return carLicensePlate; }
    public void setCarLicensePlate(String carLicensePlate) { this.carLicensePlate = carLicensePlate; }

    public String getStatusName() { return statusName; }
    public void setStatusName(String statusName) { this.statusName = statusName; }
}
