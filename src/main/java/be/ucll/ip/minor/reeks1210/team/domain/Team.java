package be.ucll.ip.minor.reeks1210.team.domain;

import be.ucll.ip.minor.reeks1210.regatta.domain.Regatta;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "team", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "category"})
})
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "passengers")
    private int passengers;

    @Column(name = "club")
    private String club;

    @ManyToMany(mappedBy = "teams")
    private Set<Regatta> regattaNames;


    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }
    public int getPassengers() {
        return passengers;
    }

    public void setClub(String club) {
        this.club = club;
    }
    public String getClub() {
        return club;
    }

    public Set<Regatta> getRegattaNames() {
        if (regattaNames == null || regattaNames.isEmpty()) {
            regattaNames = new HashSet<>();
        }

        return regattaNames;
    }

    public void setRegattaNames(Set<Regatta> regs){
        this.regattaNames = regs;
    }

    public void addRegatta(Regatta regatta) {
        this.getRegattaNames().add(regatta);
    }

    public void removeRegatta(Regatta regatta){
        if(this.getRegattaNames().contains(regatta)){
            this.regattaNames.remove(regatta);
        }
    }

}
