package be.ucll.ip.minor.reeks1210.Boat;

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
public class BoatEnd2EndTest {


    @Autowired
    private WebTestClient client;

    @Test
    public void validateThatA400IsReturnedWhenANonValidNameIsUsed() {
        BoatBodyValue value = new BoatBodyValue();
        value.name = "bas";
        value.email = "bas@kas.be";
        value.length = 11;
        value.width = 11;
        value.height = 11;
        value.insuranceNumber = "123456789A";


        client.post()
                .uri("/api/boat/add")
                .bodyValue(value)
                .exchange()
                .expectBody()
                .json("{\"name\":\"name.too.short\"}");
    }

    @Test
    public void validateThatA400IsReturnedWhenANonValidInsuranceNumberIsUsed() {
        BoatBodyValue value = new BoatBodyValue();
        value.name = "baske";
        value.email = "bas@kas.be";
        value.length = 11;
        value.width = 11;
        value.height = 11;
        value.insuranceNumber = "incorrect";


        client.post()
                .uri("/api/boat/add")
                .bodyValue(value)
                .exchange()
                .expectBody()
                .json("{\"insuranceNumber\":\"invalid.insurance.number\"}");
    }

    @Test
    public void validateThatA400IsReturnedWhenANonValidEmailIsUsed() {
        BoatBodyValue value = new BoatBodyValue();
        value.name = "baske";
        value.email = "baskas.be";
        value.length = 11;
        value.width = 11;
        value.height = 11;
        value.insuranceNumber = "123456789A";


        client.post()
                .uri("/api/boat/add")
                .bodyValue(value)
                .exchange()
                .expectBody()
                .json("{\"email\":\"invalid.email\"}");
    }

    private static class BoatBodyValue {


        public String name;
        public String email;
        public double length;
        public double width;
        public double height;
        public String insuranceNumber;
    }


}
