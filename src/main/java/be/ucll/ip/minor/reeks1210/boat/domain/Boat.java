package be.ucll.ip.minor.reeks1210.boat.domain;

import be.ucll.ip.minor.reeks1210.general.ServiceException;
import be.ucll.ip.minor.reeks1210.storage.domain.Storage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "boat", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"insuranceNumber","name","email"}),


})
public class Boat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //relations
    @ManyToOne
    @JoinTable(name="storage_id")
    @JsonIgnoreProperties("boats")
    private Storage storage;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "length")
    private double length;

    @Column(name = "width")
    private double width;

    @Column(name = "height")
    private double height;

    @Column(name = "insuranceNumber")
    private String insuranceNumber;

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    // RELATIONS
    public Storage getStorage() {
        return storage;
    }

    // RELATIONS
    public void setStorage(Storage storage) {
        this.storage = storage;
    }


    public void boatFitsInStorage(){
        Storage storage = this.getStorage();
        if (this.getHeight() > Double.parseDouble(storage.getHeight())){
            throw new ServiceException("Boat", "Boat is too high for this storage");
        }
        if(this.getWidth() * this.getLength()> Double.parseDouble(storage.getSurface())){
            throw new ServiceException("Boat", "Boat is too big for this storage");
        }
    }
}
