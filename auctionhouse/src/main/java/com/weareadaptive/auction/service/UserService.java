package com.weareadaptive.auction.service;

import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.model.UserState;
import org.springframework.stereotype.Service;

import java.net.PortUnreachableException;

@Service
public class UserService {
  private final UserState userState;

  public UserService(UserState userState) {
    this.userState = userState;
  }



  public User create(String username, String password, String firstName, String lastName,
                     String organisation) {

    if(userState.getByUsername(username).isPresent()){
      throw new RuntimeException("username already exist");
    }

    var userID = userState.nextId();
    var user = new User(userID, username, password, firstName, lastName, organisation);
    userState.add(user);

    return user;
//    throw new UnsupportedOperationException();
  }

  public UserState getUserState(){
    return this.userState;
  }

}
