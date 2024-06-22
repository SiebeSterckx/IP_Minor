package be.ucll.ip.minor.reeks1210.regatta.controller;

import be.ucll.ip.minor.reeks1210.regattaTeam.RegattaTeamDto;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public class RegattaDto {

    private long id;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @NotEmpty(message = "name.missing")
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @NotEmpty(message = "clubname.missing")
    private String clubName;
    public String getClubName() {
        return clubName;
    }
    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    @Future(message = "date.future")
    @NotNull(message = "date.missing")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Min(value = 1, message = "maxteams.min")
    private int maxTeams;
    public int getMaxTeams() {
        return maxTeams;
    }
    public void setMaxTeams(int maxTeams) {
        this.maxTeams = maxTeams;
    }

    @NotEmpty(message = "category.missing")
    private String category;
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private List<RegattaTeamDto> teams;

    public List<RegattaTeamDto> getteams() {
        return teams;
    }

    public void setTeams(List<RegattaTeamDto> teams) {
        this.teams = teams;
    }
}

