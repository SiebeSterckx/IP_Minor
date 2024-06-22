package be.ucll.ip.minor.reeks1210.Boat;

import be.ucll.ip.minor.reeks1210.boat.controller.BoatDto;
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

public class BoatDtoTest {

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
    public void givenValidBoat_shouldHaveNoViolations() {
        //given
        BoatDto Banana = BoatDtoBuilder.aBoatBanana().build();

        //when
        Set<ConstraintViolation<BoatDto>> violations = validator.validate(Banana);

        //then
        assertTrue(violations.isEmpty());
    }

    @Test
    public void givenBoatWithEmptyName_shouldDetectInvalidNameError() {
        //given
        BoatDto invalid = BoatDtoBuilder.anInvalidBoatWithNoName().build();

        //when
        Set<ConstraintViolation<BoatDto>> violations = validator.validate(invalid);

        //then
        assertEquals(violations.size(), 2);
        ConstraintViolation<BoatDto> violation = violations.iterator().next();
        assertThat(violation.getMessage(), anyOf(is("name.missing"), is("name.too.short")));
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }


}
