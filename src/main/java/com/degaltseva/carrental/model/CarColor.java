package com.degaltseva.carrental.model;

public class CarColor {

    private Long id;
    private String color;
    private String hex;

    public CarColor() {}

    public CarColor(Long id, String color, String hex) {
        this.id = id;
        this.color = color;
        this.hex = hex;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getHex() { return hex; }
    public void setHex(String hex) { this.hex = hex; }
}
