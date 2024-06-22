package be.ucll.ip.minor.reeks1210.storage.controller;

import jakarta.validation.constraints.*;

public class StorageDto {

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

    @NotBlank(message = "postal.missing")  // use regex for validation code must be between 1000 and 9999
    @Pattern(regexp = "^[1-9][0-9]{3}$", message = "invalid.postal.code")
    private String postal;

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getPostal() {
        return postal;
    }

    @Pattern(regexp = "^([1-9]\\d{0,2}(\\.\\d+)?|1000\\.0+|2000\\.0*|[12]\\d{3}(\\.0+)?)$", message = "surface.invalid")
    private String surface; // m²

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getSurface() {
        return surface;
    }

    private String height; // m

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHeight() {
        return height;
    }
}
