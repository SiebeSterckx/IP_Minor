package be.ucll.ip.minor.reeks1210.Boat;

import be.ucll.ip.minor.reeks1210.boat.domain.Boat;

public class BoatBuilder {

    private long id;
    private String name;
    private String email;
    private double length;
    private double width;
    private double height;
    private String insuranceNumber;

    private BoatBuilder() {
    }

    public static BoatBuilder aBoat(){
        return new BoatBuilder();
    }

    public static BoatBuilder aBoatBanana() {
        return aBoat().withId(1).withName("Banana").withEmail("Anna@ucll.be").withLength(4).withWidth(5).withHeight(6).withInsuranceNumber("123456789A");
    }

    public static BoatBuilder aBoatApple() {
        return aBoat().withId(2).withName("Apple").withEmail("Joske@ucll.be").withLength(5).withWidth(6).withHeight(7).withInsuranceNumber("123456789B");
    }

    public static BoatBuilder aBoatGrape() {
        return aBoat().withId(3).withName("Grape").withEmail("Patrick@ucll.be").withLength(6).withWidth(7).withHeight(8).withInsuranceNumber("123456789C");
    }


    public static BoatBuilder anInvalidBoatWithNoName() {
        return aBoat().withId(4).withName("").withEmail("Patrick@ucll.be").withLength(6).withWidth(7).withHeight(8).withInsuranceNumber("123456789C");
    }


    public BoatBuilder withId (long id) {
        this.id = id;
        return this;
    }

    public BoatBuilder withName (String name) {
        this.name = name;
        return this;
    }

    public BoatBuilder withEmail (String email) {
        this.email = email;
        return this;
    }

    public BoatBuilder withLength (double length) {
        this.length = length;
        return this;
    }

    public BoatBuilder withWidth (double width){
        this.width = width;
        return this;
    }

    public BoatBuilder withHeight (double height){
        this.height = height;
        return this;
    }

    public BoatBuilder withInsuranceNumber (String insuranceNumber){
        this.insuranceNumber = insuranceNumber;
        return this;
    }

    public Boat build() {
        Boat boat = new Boat();
        boat.setId(id);
        boat.setName(name);
        boat.setEmail(email);
        boat.setLength(length);
        boat.setWidth(width);
        boat.setHeight(height);
        boat.setInsuranceNumber(insuranceNumber);
        return boat;
    }

}