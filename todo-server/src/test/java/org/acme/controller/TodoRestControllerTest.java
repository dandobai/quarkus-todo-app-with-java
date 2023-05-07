package org.acme.controller;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class TodoRestControllerTest {
    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/bello")
          .then()
             .statusCode(200)
             .body(is("Hello from RESTEasy Reactive"));
    }
}
