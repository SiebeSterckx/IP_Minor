package be.ucll.ip.minor.reeks1210.Boat;

import be.ucll.ip.minor.reeks1210.boat.domain.Boat;
import be.ucll.ip.minor.reeks1210.boat.domain.BoatRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest

public class BoatRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BoatRepository boatRepository;


    @Test
    public void givenBoatsCreated_whenFindByEmail_thenAllBoatsFoundAreReturned() {
        // given
        Boat banana = BoatBuilder.aBoatBanana().build();
        entityManager.merge(banana);
        Boat apple = BoatBuilder.aBoatApple().build();
        entityManager.merge(apple);
        Boat Grape = BoatBuilder.aBoatGrape().build();
        entityManager.merge(Grape);

        // when
        Iterable<Boat> boats = boatRepository.findAllByInsuranceNumber("123456789A");

        // then
        assertEquals(1, ((List<Boat>) boats).size());
    }

    @Test
    public void givenBoatsCreated_whenFindByHeightAndWidth_thenAllBoatsFoundAreReturned() {
        // given
        Boat banana = BoatBuilder.aBoatBanana().build();
        entityManager.merge(banana);
        Boat apple = BoatBuilder.aBoatApple().build();
        entityManager.merge(apple);
        Boat Grape = BoatBuilder.aBoatGrape().build();
        entityManager.merge(Grape);

        // when
        Iterable<Boat> boats = boatRepository.findAllByHeightAndAndWidth(6,5);

        // then
        assertEquals(1, ((List<Boat>) boats).size());
    }



}
