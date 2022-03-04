package com.weareadaptive.auction.controller;

import com.github.javafaker.Faker;
import com.weareadaptive.auction.TestData;
import com.weareadaptive.auction.controller.dto.CreateAuctionRequest;
import com.weareadaptive.auction.model.AuctionLot;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuctionControllerTest {
  public final int INVALID_AUCTION_ID = 99999;
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
        testData.auctionOne().getSymbol(),
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
    AuctionLot auctionApple = testData.auctionOne();
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .pathParam("id", auctionApple.getId())
        .when()
        .get("/auctions/{id}")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("id", equalTo(testData.auctionOne().getId()))
        .body("symbol", equalTo(testData.auctionOne().getSymbol()))
        .body("owner", equalTo(testData.auctionOne().getOwner().getUsername()))
        .body("quantity", equalTo(testData.auctionOne().getQuantity()))
        .body("minPrice", equalTo((float)testData.auctionOne().getMinPrice()))
        .body("status", equalTo(testData.auctionOne().getStatus().toString()));
  }



  @DisplayName("get auction return bad request if ID not exist")
  @Test
  public void returnBadRequestIfAuctionIDNotExists(){
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .pathParam("id", INVALID_AUCTION_ID)
        .when()
        .get("/auctions/{id}")
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }


  @DisplayName("get all auctions when request all")
  @Test
  public void returnAllAuctions(){
    var found1 = format("find { it.id == %s }.", testData.auctionOne().getId());
    var found2 = format("find { it.id == %s }.", testData.auctionTwo().getId());
    var found3 = format("find { it.id == %s }.", testData.auctionThree().getId());

    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .when()
        .get("/auctions")
        .then()
        .statusCode(HttpStatus.OK.value())
        // validate auction one
        .body(found1+"symbol", equalTo(testData.auctionOne().getSymbol()))
        .body(found1+"owner", equalTo(testData.auctionOne().getOwner().getUsername()))
        .body(found1+"quantity", equalTo(testData.auctionOne().getQuantity()))
        .body(found1+"minPrice", equalTo((float)testData.auctionOne().getMinPrice()))
        .body(found1+"status", equalTo(testData.auctionOne().getStatus().toString()))
        // validate auction two
        .body(found2+"symbol", equalTo(testData.auctionTwo().getSymbol()))
        .body(found2+"owner", equalTo(testData.auctionTwo().getOwner().getUsername()))
        .body(found2+"quantity", equalTo(testData.auctionTwo().getQuantity()))
        .body(found2+"minPrice", equalTo((float)testData.auctionTwo().getMinPrice()))
        .body(found2+"status", equalTo(testData.auctionTwo().getStatus().toString()))
        // validate auction three
        .body(found3+"symbol", equalTo(testData.auctionThree().getSymbol()))
        .body(found3+"owner", equalTo(testData.auctionThree().getOwner().getUsername()))
        .body(found3+"quantity", equalTo(testData.auctionThree().getQuantity()))
        .body(found3+"minPrice", equalTo((float)testData.auctionThree().getMinPrice()))
        .body(found3+"status", equalTo(testData.auctionThree().getStatus().toString()));

  }


}