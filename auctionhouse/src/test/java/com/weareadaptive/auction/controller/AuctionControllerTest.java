package com.weareadaptive.auction.controller;

import com.github.javafaker.Faker;
import com.weareadaptive.auction.TestData;
import com.weareadaptive.auction.controller.dto.CreateAuctionRequest;
import com.weareadaptive.auction.model.AuctionLot;
import io.restassured.http.ContentType;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuctionControllerTest {

  @Autowired
  private TestData testData;
  @LocalServerPort
  private int port;
  private String uri;
  private final Faker faker = new Faker();

  @BeforeEach
  public void initialiseRestAssuredMockMvcStandalone() {
    uri = "http://localhost:" + port;
  }

  @DisplayName("create should create and return the new auction")
  @Test
  public void shouldReturnAuctionIfCreated() {
    var createAuctionRequest = new CreateAuctionRequest(
        "ADAPT",
        2.23,
        200
    );

    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .contentType(ContentType.JSON)
        .body(createAuctionRequest)
        .when()
        .post("/auctions")
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .body("id", greaterThan(0))
        .body("owner", equalTo(testData.user1().getUsername()))
        .body("symbol", equalTo(createAuctionRequest.symbol()))
        .body("minPrice", equalTo((float) createAuctionRequest.minPrice()))
        .body("quantity", equalTo(createAuctionRequest.quantity()));
  }

  @DisplayName("create should return a bad request when the symbol is duplicated")
  @Test
  public void create_shouldReturnBadRequestIfSymbolExist() {
    var createAuctionRequest = new CreateAuctionRequest(
        testData.auctionApple().getSymbol(),
        2.23,
        200
    );

    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .contentType(ContentType.JSON)
        .body(createAuctionRequest)
        .when()
        .post("/auctions")
        .then()
        .statusCode(BAD_REQUEST.value())
        .body("message", containsString("already exist"));

  }

  @DisplayName("return an auction by id")
  @Test
  public void returnAuctionGetByID(){
    AuctionLot auctionApple = testData.auctionApple();
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .pathParam("id", auctionApple.getId())
        .when()
        .get("/auctions/{id}")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("id", equalTo(testData.auctionApple().getId()))
        .body("symbol", equalTo(testData.auctionApple().getSymbol()))
        .body("owner", equalTo(testData.auctionApple().getOwner().getUsername()))
        .body("quantity", equalTo(testData.auctionApple().getQuantity()))
        .body("minPrice", equalTo((float)testData.auctionApple().getMinPrice()))
        .body("status", equalTo(testData.auctionApple().getStatus().toString()));
  }

  private Stream<Arguments> getTestDataAuctionID(){
    return Stream.of(

    );
  }


  @DisplayName("get auction return bad request if ID not exist")
  @Test
  public void returnBadRequestIfAuctionIDNotExists(){

  }

}