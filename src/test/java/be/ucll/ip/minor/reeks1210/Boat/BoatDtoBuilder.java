package be.ucll.ip.minor.reeks1210.Boat;

import be.ucll.ip.minor.reeks1210.boat.domain.Boat;
import be.ucll.ip.minor.reeks1210.boat.controller.BoatDto;


public class BoatDtoBuilder {

    private long id;
    private String name;
    private String email;
    private double length;
    private double width;
    private double height;
    private String insuranceNumber;

    private BoatDtoBuilder() {
    }

    public static BoatDtoBuilder aBoat(){
        return new BoatDtoBuilder();
    }

    public static BoatDtoBuilder aBoatBanana() {
        return aBoat().withId(1).withName("Banana").withEmail("Anna@ucll.be").withLength(4).withWidth(5).withHeight(6).withInsuranceNumber("123456789A");
    }

    public static BoatDtoBuilder aBoatApple() {
        return aBoat().withId(2).withName("Apple").withEmail("Joske@ucll.be").withLength(5).withWidth(6).withHeight(7).withInsuranceNumber("123456789B");
    }

    public static BoatDtoBuilder aBoatGrape() {
        return aBoat().withId(3).withName("Grape").withEmail("Patrick@ucll.be").withLength(10).withWidth(7).withHeight(8).withInsuranceNumber("123456789C");
    }


    public static BoatDtoBuilder anInvalidBoatWithNoName() {
        return aBoat().withId(4).withName("").withEmail("Patrick@ucll.be").withLength(6).withWidth(7).withHeight(8).withInsuranceNumber("123456789C");
    }


    public BoatDtoBuilder withId (long id) {
        this.id = id;
        return this;
    }

    public BoatDtoBuilder withName (String name) {
        this.name = name;
        return this;
    }

    public BoatDtoBuilder withEmail (String email) {
        this.email = email;
        return this;
    }

    public BoatDtoBuilder withLength (double length) {
        this.length = length;
        return this;
    }

    public BoatDtoBuilder withWidth (double width){
        this.width = width;
        return this;
    }

    public BoatDtoBuilder withHeight (double height){
        this.height = height;
        return this;
    }

    public BoatDtoBuilder withInsuranceNumber (String insuranceNumber){
        this.insuranceNumber = insuranceNumber;
        return this;
    }

    public BoatDto build() {
        BoatDto boat = new BoatDto();
        boat.setId(id);
        boat.setName(name);
        boat.setEmail(email);
        boat.setLength(length);
        boat.setWidth(width);
        boat.setHeight(height);
        boat.setInsuranceNumber(insuranceNumber);
        return boat;
    }

    public BoatDtoBuilder getBoatWithId(Long id) {
        BoatDto boat = build();
        boat.setId(id);
        return this;
    }


}