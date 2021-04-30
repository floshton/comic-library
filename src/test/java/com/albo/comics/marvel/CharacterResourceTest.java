package com.albo.comics.marvel;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class CharacterResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/characters")
          .then()
             .statusCode(200);
    }

}