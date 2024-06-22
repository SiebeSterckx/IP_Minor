package be.ucll.ip.minor.reeks1210.Team;

import be.ucll.ip.minor.reeks1210.team.domain.Team;

public class TeamBuilder {

    private long id;
    private String name;
    private String category;
    private int passengers;
    private String club;

    private TeamBuilder() {
    }

    public static TeamBuilder aTeam(){
        return new TeamBuilder();
    }

    public static TeamBuilder aTeamRacers() {
        return aTeam().withId(1).withName("Racers").withCategory("Power01").withOccupants(6).withClub("Racing Club");
    }

    public static TeamBuilder aTeamSpeeders() {
        return aTeam().withId(2).withName("Speeders").withCategory("Sailing").withOccupants(2).withClub("Speeding Club");
    }

    public static TeamBuilder aTeamSurfers() {
        return aTeam().withId(3).withName("Surfers").withCategory("Surfing").withOccupants(10).withClub("Surfing Club");
    }

    public static TeamBuilder aTeamSurfers2() {
        return aTeam().withId(6).withName("Surfers2").withCategory("Surfing").withOccupants(7).withClub("Surfing Club2");
    }

    public static TeamBuilder aTeamWithNoClub() {
        return aTeam().withId(4).withName("Lone Wolf").withCategory("Sailing").withOccupants(8).withClub("");
    }

    public static TeamBuilder anInvalidTeamWithNoName() {
        return aTeam().withId(5).withName("").withCategory("Power01").withOccupants(5).withClub("Nameless Club");
    }


    public TeamBuilder withId (long id) {
        this.id = id;
        return this;
    }

    public TeamBuilder withName (String name) {
        this.name = name;
        return this;
    }

    public TeamBuilder withCategory (String category) {
        this.category = category;
        return this;
    }

    public TeamBuilder withOccupants (int occupants) {
        this.passengers = occupants;
        return this;
    }

    public TeamBuilder withClub (String club){
        this.club = club;
        return this;
    }

    public Team build() {
        Team team = new Team();
        team.setId(id);
        team.setName(name);
        team.setCategory(category);
        team.setPassengers(passengers);
        team.setClub(club);
        return team;
    }

}
