
package be.ucll.ip.minor.reeks1210.regatta.domain;

import be.ucll.ip.minor.reeks1210.general.ServiceException;
import be.ucll.ip.minor.reeks1210.team.domain.Team;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "regatta", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name","clubName", "date"})
})
public class Regatta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="name")
    private String name;
    @Column(name = "clubName")
    private String clubName;
    @Column(name="date")
    private LocalDate date;
    @Column(name="maxTeams")
    private int maxTeams;
    @Column(name="category")
    private String category;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "regatta_team",
            joinColumns = @JoinColumn(name = "regatta_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private Set<Team> teams;


    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getMaxTeams() {
        return maxTeams;
    }

    public void setMaxTeams(int maxTeams) {
        this.maxTeams = maxTeams;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public Set<Team> getTeams() {
        if (teams == null) {
            this.teams = new HashSet<>();
        }
        return teams;
    }

    public void setTeams(Set<Team> teams){
        this.teams = teams;
    }

    public void addTeam(Team team) {
        for(Regatta regatta1: team.getRegattaNames()){
            if(regatta1.getDate().equals(this.getDate())){throw new ServiceException("AddTeam","regattaTeam.schedule");}
        }
        if(this.getMaxTeams() <= this.getTeams().size()){throw new ServiceException("AddTeam","regattaTeam.maxteams");}

        if(!this.getCategory().equals(team.getCategory())){throw new ServiceException("AddTeam","regattaTeam.wrongcat");}

        this.getTeams().add(team);
        team.addRegatta(this);
    }

    public void addableTeam(Team team){
        for(Regatta regatta1: team.getRegattaNames()){
            if(regatta1.getDate().equals(this.getDate()) && regatta1.getId() != this.getId()){throw new ServiceException("AddableTeamInRegatta","regattaTeam.schedule");}
        }
        if(!this.getCategory().equals(team.getCategory())){throw new ServiceException("AddableTeamInRegatta", "regattaTeam.noupdate");}
    }

    public void removeTeam(Team team){
        if(!this.getTeams().contains(team)){
            throw new ServiceException("RemoveTeam","regattaTeam.notin");
        }
        this.teams.remove(team);
        team.removeRegatta(this);
    }
}

