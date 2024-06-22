package be.ucll.ip.minor.reeks1210.Team;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional // Cleans-up after test is finished
public class TeamEnd2EndTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void validateThatA400IsReturnedWhenANonValidNameIsUsed() {
        TeamBodyValue value = new TeamBodyValue();
        value.name = "ff";
        value.category = "Sailing";
        value.passengers = 2;
        value.club = "End2End Club1";

        client.post()
                .uri("/api/team/add")
                .bodyValue(value)
                .exchange()
                .expectBody()
                .json("{\"name\":\"name.too.short\"}");
    }

    @Test
    public void validateThatA400IsReturnedWhenANonValidCategoryIsUsed() {
        TeamBodyValue value = new TeamBodyValue();
        value.name = "tester2";
        value.category = "5g5";
        value.passengers = 2;
        value.club = "End2End Club2";

        client.post()
                .uri("/api/team/add")
                .bodyValue(value)
                .exchange()
                .expectBody()
                .json("{\"category\":\"category.invalid\"}");
    }

    @Test
    public void validateThatA400IsReturnedWhenANonValidOccupantsIsUsed() {
        TeamBodyValue value = new TeamBodyValue();
        value.name = "tester3";
        value.category = "Sailing";
        value.passengers = 88;
        value.club = "End2End Club3";

        client.post()
                .uri("/api/team/add")
                .bodyValue(value)
                .exchange()
                .expectBody()
                .json("{\"passengers\":\"passengers.too.big\"}");
    }

    private static class TeamBodyValue {

        public String name;
        public String category;
        public int passengers;
        public String club;
    }
}
