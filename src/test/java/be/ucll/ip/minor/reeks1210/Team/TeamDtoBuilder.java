package be.ucll.ip.minor.reeks1210.Team;

import be.ucll.ip.minor.reeks1210.team.controller.TeamDto;

public class TeamDtoBuilder {

    private long id;
    private String name;
    private String category;
    private int passengers;
    private String club;

    private TeamDtoBuilder() {
    }

    public static TeamDtoBuilder aTeam(){
        return new TeamDtoBuilder();
    }

    public static TeamDtoBuilder aTeamRacers() {
        return aTeam().withId(1).withName("Racers").withCategory("Power01").withOccupants(6).withClub("Racing Club");
    }

    public static TeamDtoBuilder aTeamSpeeders() {
        return aTeam().withId(2).withName("Speeders").withCategory("Sailing").withOccupants(2).withClub("Speeding Club");
    }

    public static TeamDtoBuilder aTeamSurfers() {
        return aTeam().withId(3).withName("Surfers").withCategory("Surfing").withOccupants(10).withClub("Surfing Club");
    }
    public static TeamDtoBuilder aTeamSurfers2() {
        return aTeam().withId(6).withName("Surfers2").withCategory("Surfing").withOccupants(7).withClub("Surfing Club2");
    }


    public static TeamDtoBuilder aTeamWithNoClub() {
        return aTeam().withId(4).withName("Lone Wolf").withCategory("Sailing").withOccupants(8).withClub("");
    }

    public static TeamDtoBuilder anInvalidTeamWithNoName() {
        return aTeam().withId(5).withName("").withCategory("Power01").withOccupants(5).withClub("Nameless Club");
    }


    public TeamDtoBuilder withId (long id) {
        this.id = id;
        return this;
    }

    public TeamDtoBuilder withName (String name) {
        this.name = name;
        return this;
    }

    public TeamDtoBuilder withCategory (String category) {
        this.category = category;
        return this;
    }

    public TeamDtoBuilder withOccupants (int occupants) {
        this.passengers = occupants;
        return this;
    }

    public TeamDtoBuilder withClub (String club){
        this.club = club;
        return this;
    }

    public TeamDto build() {
        TeamDto team = new TeamDto();
        team.setName(name);
        team.setCategory(category);
        team.setPassengers(passengers);
        team.setClub(club);
        return team;
    }

    public TeamDtoBuilder getTeamWithId(Long id) {
        TeamDto team = build();
        team.setId(id);
        return this;
    }
}
