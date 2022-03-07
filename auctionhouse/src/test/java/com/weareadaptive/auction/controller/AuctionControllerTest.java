package com.weareadaptive.auction.controller;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.weareadaptive.auction.TestData;
import com.weareadaptive.auction.controller.dto.CreateAuctionRequest;
import com.weareadaptive.auction.controller.dto.CreateBidRequest;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.Bid;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuctionControllerTest {
  public final int INVALID_AUCTION_ID = 99999;
  @Autowired
  private TestData testData;
  @LocalServerPort
  private int port;
  private String uri;

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
        testData.auctionUser1Apple().getSymbol(),
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
  public void returnAuctionGetByID() {
    AuctionLot auctionApple = testData.auctionUser1Apple();
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .pathParam("id", auctionApple.getId())
        .when()
        .get("/auctions/{id}")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("id", equalTo(testData.auctionUser1Apple().getId()))
        .body("symbol", equalTo(testData.auctionUser1Apple().getSymbol()))
        .body("owner", equalTo(testData.auctionUser1Apple().getOwner().getUsername()))
        .body("quantity", equalTo(testData.auctionUser1Apple().getQuantity()))
        .body("minPrice", equalTo((float) testData.auctionUser1Apple().getMinPrice()))
        .body("status", equalTo(testData.auctionUser1Apple().getStatus().toString()));
  }


  @DisplayName("get auction return bad request if ID not exist")
  @Test
  public void returnBadRequestIfAuctionIDNotExists() {
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
  public void returnAllAuctions() {
    var found1 = format("find { it.id == %s }.", testData.auctionUser1Apple().getId());
    var found2 = format("find { it.id == %s }.", testData.auctionUser2MSFT().getId());
    var found3 = format("find { it.id == %s }.", testData.auctionUser1FB().getId());

    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .when()
        .get("/auctions")
        .then()
        .statusCode(HttpStatus.OK.value())
        // body ( assertAll( ... ))
        // validate auction one
        .body(found1 + "symbol", equalTo(testData.auctionUser1Apple().getSymbol()))
        .body(found1 + "owner", equalTo(testData.auctionUser1Apple().getOwner().getUsername()))
        .body(found1 + "quantity", equalTo(testData.auctionUser1Apple().getQuantity()))
        .body(found1 + "minPrice", equalTo((float) testData.auctionUser1Apple().getMinPrice()))
        .body(found1 + "status", equalTo(testData.auctionUser1Apple().getStatus().toString()))
        // validate auction two
        .body(found2 + "symbol", equalTo(testData.auctionUser2MSFT().getSymbol()))
        .body(found2 + "owner", equalTo(testData.auctionUser2MSFT().getOwner().getUsername()))
        .body(found2 + "quantity", equalTo(testData.auctionUser2MSFT().getQuantity()))
        .body(found2 + "minPrice", equalTo((float) testData.auctionUser2MSFT().getMinPrice()))
        .body(found2 + "status", equalTo(testData.auctionUser2MSFT().getStatus().toString()))
        // validate auction three
        .body(found3 + "symbol", equalTo(testData.auctionUser1FB().getSymbol()))
        .body(found3 + "owner", equalTo(testData.auctionUser1FB().getOwner().getUsername()))
        .body(found3 + "quantity", equalTo(testData.auctionUser1FB().getQuantity()))
        .body(found3 + "minPrice", equalTo((float) testData.auctionUser1FB().getMinPrice()))
        .body(found3 + "status", equalTo(testData.auctionUser1FB().getStatus().toString()));

  }

  @DisplayName("return bid information when successfully bid an auctions")
  @Test
  public void returnBidWhenBidAnAuctions() {
    var createBidRequest = new CreateBidRequest(
        testData.user2().getUsername(),
        50,
        5.9
    );

    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user2Token())
        .pathParam("id", testData.auctionUser1FB().getId())
        .contentType(ContentType.JSON)
        .body(createBidRequest)
        .when()
        .post("/auctions/{id}/bid")
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .body("owner", equalTo(createBidRequest.owner()))
        .body("quantity", equalTo(createBidRequest.quantity()))
        .body("price", equalTo((float) createBidRequest.price()))
        .body("state", equalTo(Bid.State.PENDING.toString()));
  }


  @DisplayName("return bad request when bid on same owner auction")
  @Test
  public void returnBadRequestWhenBidOnOwnAuction() {
    var createBidRequest = new CreateBidRequest(
        testData.user1().getUsername(),
        50,
        5.1
    );

    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .pathParam("id", testData.auctionUser1Apple().getId())
        .contentType(ContentType.JSON)
        .body(createBidRequest)
        .when()
        .post("/auctions/{id}/bid")
        .then()
        .statusCode(BAD_REQUEST.value());
  }


  // this kind of data validation should be handled by front end ???
  @DisplayName("return 404 NOT-Found when trying to bid auction but can not find the auction")
  @Test
  public void return404WhenBiddingButAuctionNotFound() {
    var createBidRequest = new CreateBidRequest(
        testData.user2().getUsername(),
        50,
        2.3
    );

    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user2Token())
        .pathParam("id", INVALID_AUCTION_ID)
        .contentType(ContentType.JSON)
        .body(createBidRequest)
        .when()
        .post("/auctions/{id}/bid")
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }

  @DisplayName("get all bids for an auctions for this user")
  @Test
  public void getAllBidsForAnAuction() {

    List<Bid> expectedBidsList = testData.auctionUser1Apple().getBids().stream()
        .filter(bid -> bid.getUser().getUsername().equals(testData.user2().getUsername()))
        .toList();

    List<String> userList =
        expectedBidsList.stream().map(bid -> bid.getUser().getUsername()).toList();
    List<Integer> quantityList = expectedBidsList.stream()
        .map(Bid::getQuantity)
        .toList();
    List<Float> priceList = expectedBidsList.stream()
        .map(bid -> (float) bid.getPrice())
        .toList();
    List<String> stateList = expectedBidsList.stream()
        .map(bid -> bid.getState().toString())
        .toList();

    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user2Token())
        .pathParam("id", testData.auctionUser1Apple().getId())
        .when()
        .get("/auctions/{id}/bids/get-all")
        .then()
        .statusCode(HttpStatus.OK.value())
        // body ( assertAll( ... ))
        // validate bid one
        .body(
            "owner", equalTo(userList),
            "quantity", equalTo(quantityList),
            "price", equalTo(priceList),
            "state", equalTo(stateList)
        );
  }

  @DisplayName("return brief closing summary when close an auction")
  @Test
  public void returnBriefClosingSummaryWhenCloseAuction() {
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .pathParam("id", testData.auctionUser1Apple().getId())
        .when()
        .get("/auctions/{id}/close")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body(
            "totalSoldQuantity", equalTo(125),
            "totalRevenue", equalTo((float) 691.5),
            "closingTime", containsString("2022")
        );
  }




}