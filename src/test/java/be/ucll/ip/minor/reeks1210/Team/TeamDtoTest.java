package be.ucll.ip.minor.reeks1210.Team;


import be.ucll.ip.minor.reeks1210.team.controller.TeamDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TeamDtoTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void givenValidTeam_shouldHaveNoViolations() {
        //given
        TeamDto team1 = TeamDtoBuilder.aTeamRacers().build();

        //when
        Set<ConstraintViolation<TeamDto>> violations = validator.validate(team1);

        //then
        assertTrue(violations.isEmpty());
    }

    @Test
    public void givenTeamWithEmptyName_shouldDetectMissingNameError() {
        //given
        TeamDto team2 = TeamDtoBuilder.anInvalidTeamWithNoName().build();

        //when
        Set<ConstraintViolation<TeamDto>> violations = validator.validate(team2);

        //then
        assertEquals(violations.size(), 2);
        ConstraintViolation<TeamDto> violation = violations.iterator().next();
        assertThat(violation.getMessage(), anyOf(is("name.missing"), is("name.too.short")));
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }
}