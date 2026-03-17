package com.degaltseva.carrental.model;

import java.time.LocalDate;

public class Client {

    private Long id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String phone;
    private String email;
    private String driverLicense;
    private LocalDate licenseExpiryDate;
    private Long userId;

    // ClientService
    private String username;

    public Client() {}

    public Client(Long id, String name, String surname, LocalDate birthDate,
                  String phone, String email, String driverLicense,
                  LocalDate licenseExpiryDate, Long userId) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.phone = phone;
        this.email = email;
        this.driverLicense = driverLicense;
        this.licenseExpiryDate = licenseExpiryDate;
        this.userId = userId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDriverLicense() { return driverLicense; }
    public void setDriverLicense(String driverLicense) { this.driverLicense = driverLicense; }

    public LocalDate getLicenseExpiryDate() { return licenseExpiryDate; }
    public void setLicenseExpiryDate(LocalDate licenseExpiryDate) { this.licenseExpiryDate = licenseExpiryDate; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
