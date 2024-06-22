package be.ucll.ip.minor.reeks1210.boat.controller;

import jakarta.validation.constraints.*;

public class BoatDto {

    private long id;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    // combination name postal is unique

    @NotBlank(message = "name.missing") // name cannot be empty and must be at least 5 characters long
    @Size(min = 5, message = "name.too.short")
    private String name;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    @NotBlank(message = "insuranceNumber.missing")  // use regex for validation, number must be 10 chars long
    @Pattern(regexp = "^[A-Za-z0-9]{10}$", message = "invalid.insurance.number")
    private String insuranceNumber;
    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }
    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    @NotBlank(message = "email.missing")  // use regex for validation, email must be filled in correctly
    @Email(message = "invalid.email")
    private String email;
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }


    @Min(value = 1, message = "length.too.small") // cannot be less than 1
    private double length; // m
    public void setLength(double length) {this.length = length;}
    public double getLength() {return length;}

    @Min(value = 1, message = "height.too.small") // cannot be less than 1
    private double height; // m
    public void setHeight(double height) {
        this.height = height;
    }
    public double getHeight() {
        return height;
    }

    @Min(value = 1, message = "width.too.small") // cannot be less than 1
    private double width; // m
    public void setWidth(double width) {
        this.width = width;
    }
    public double getWidth() {
        return width;
    }
}
