package com.weareadaptive.auction;

import com.github.javafaker.Faker;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.Bid;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.service.AuctionLotService;
import com.weareadaptive.auction.service.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TestData {
  public static final String PASSWORD = "mypassword";
  public static final String ADMIN_AUTH_TOKEN = "Bearer ADMIN:adminpassword";

  private final UserService userService;
  private final AuctionLotService auctionLotService;
  private final Faker faker;
  private User user1;
  private User user2;
  private User user3;
  private User user4;

  private AuctionLot auctionApple;
  private AuctionLot auctionMSFT;
  private AuctionLot auctionFB;

  private Bid bidOneUser2;
  private Bid bidTwoUser2;
  private Bid bidThreeUser2;
  private Bid bidFourUser3;


  public TestData(UserService userService, AuctionLotService auctionLotService) {
    this.userService = userService;
    this.auctionLotService = auctionLotService;
    faker = new Faker();
  }

  @EventListener(ApplicationReadyEvent.class)
  public void createInitData() {
    user1 = createRandomUser();
    user2 = createRandomUser();
    user3 = createRandomUser();
    user4 = createRandomUser();

    auctionApple = createAuction(Stock.APPLE.symbol);
    auctionMSFT = createAuction(Stock.MICROSOFT.symbol);
    auctionFB = createAuction(Stock.META.symbol);

    bidOneUser2 = bidOneUser2();
    bidTwoUser2 = bidTwoUser2();
    bidThreeUser2 = bidThreeUser2();
    bidFourUser3 = bidFourUser3();

    auctionApple.bid(bidOneUser2);
    auctionApple.bid(bidTwoUser2);
    auctionApple.bid(bidThreeUser2);
    auctionApple.bid(bidFourUser3);

  }

  public User user1() {
    return user1;
  }

  public User user2() {
    return user2;
  }

  public User user3() {
    return user3;
  }

  public User user4() {
    return user4;
  }

  public String user1Token() {
    return getToken(user1);
  }

  public String user2Token() {
    return getToken(user2);
  }

  public String user3Token() {
    return getToken(user3);
  }

  public String user4Token() {
    return getToken(user4);
  }

  public Bid bidOneUser2(){
    return new Bid(user2, 20, 4.2);
  }
  public Bid bidTwoUser2(){
    return new Bid(user2, 30, 4.5);
  }

  public Bid bidThreeUser2(){
    return new Bid(user2, 45, 5.9);
  }

  public Bid bidFourUser3(){
    return new Bid(user3, 30, 6.9);
  }

  public AuctionLot auctionOne(){
    return auctionApple;
  }
  public AuctionLot auctionTwo(){
    return auctionMSFT;
  }
  public AuctionLot auctionThree(){
    return auctionFB;
  }


  public AuctionLot createAuction(String symbol){
    return auctionLotService.createAuction(
      user1.getUsername(),
      symbol,
      faker.number().numberBetween(100, 200),
      faker.number().randomDouble(2, 1,4));
  }

  public User createRandomUser() {
    var name = faker.name();
    var user = userService.create(
        name.username(),
        PASSWORD,
        name.firstName(),
        name.lastName(),
        faker.company().name()
    );
    return user;
  }

  public String getToken(User user) {
    return "Bearer " + user.getUsername() + ":" + PASSWORD;
  }

  public enum Stock {
    APPLE("AAPL"),
    MICROSOFT("MSFT"),
    META("FB");

    private final String symbol;

    Stock(String symbol) {
      this.symbol = symbol;
    }
  }
}
