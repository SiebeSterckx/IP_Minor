package be.ucll.ip.minor.reeks1210.storage.domain;

import be.ucll.ip.minor.reeks1210.boat.domain.Boat;
import be.ucll.ip.minor.reeks1210.general.ServiceException;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "storage", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "postal"})
})
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //relations
    @OneToMany(mappedBy = "storage", cascade = CascadeType.ALL)
    private Set<Boat> boats;

    @Column(name = "name")
    private String name;

    @Column(name = "postal")
    private String postal;

    @Column(name = "surface")
    private String surface;

    @Column(name = "height")
    private String height;

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

    public void setPostal(String postal) {
        this.postal = postal;
    }
    public String getPostal() {
        return postal;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }
    public String getSurface() {
        return surface;
    }

    public void setHeight(String height) {
        try {
            double h = Double.parseDouble(height);
            if (h < 1.0) {
                throw new IllegalArgumentException("height must be greater than 1");
            }
            else if (h > 50.0) {
                throw new IllegalArgumentException("height must be less than 50");
            }
            else {
                this.height = height;
            }
        } catch (IllegalArgumentException e) {
            throw new ServiceException("height.invalid", e.getMessage());
        }
    }
    public String getHeight() {
        return height;
    }

    // RELATIONS
    public Set<Boat> getBoats() {
        if (boats == null) {
            boats = new HashSet<>();
        }
        return boats;
    }

    // RELATIONS
    public void addBoat(Boat boat) {
        getBoats().add(boat);
        boat.setStorage(this);
    }
}
