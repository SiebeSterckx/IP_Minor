package be.ucll.ip.minor.reeks1210.team.controller;

import be.ucll.ip.minor.reeks1210.regattaTeam.RegattaTeamDto;
import jakarta.validation.constraints.*;

import java.util.List;

public class TeamDto {

    private long id;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @NotBlank(message = "name.missing")
    @Size(min = 5, message = "name.too.short")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @NotBlank(message = "category.missing")
    @Pattern(regexp = "^[a-zA-Z0-9 ]{7,}$", message = "category.invalid")
    private String category;

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    @Min(value = 1, message = "passengers.too.small")
    @Max(value = 12, message = "passengers.too.big")
    private int passengers;

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public int getPassengers() {
        return passengers;
    }

    private String club;

    public void setClub(String club) {
        this.club = club;
    }

    public String getClub() {
        return club;
    }


    private List<RegattaTeamDto> regattaNames;

    public List<RegattaTeamDto> getRegattas() {
        return regattaNames;
    }

    public void setRegattas(List<RegattaTeamDto> regattas) {
        this.regattaNames = regattas;
    }
}
