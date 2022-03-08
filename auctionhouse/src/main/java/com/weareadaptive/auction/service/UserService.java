package com.weareadaptive.auction.service;

import com.weareadaptive.auction.exception.ModelNotFoundException;
import com.weareadaptive.auction.model.KeyAlreadyExistsException;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.model.UserState;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class UserService {
  private final UserState userState;

  public UserService(UserState userState) {
    this.userState = userState;
  }


  public User create(String username, String password, String firstName, String lastName,
                     String organisation) {

    if (userState.hasUser(username)) {
      throw new KeyAlreadyExistsException("username already exist");
    }

    var userId = userState.nextId();
    var user = new User(userId, username, password, firstName, lastName, organisation);
    userState.add(user);

    return user;
  }

  public User getUserById(int id) {
    return userState.getUserById(id).orElseThrow(ModelNotFoundException::new);
  }

  public List<User> getAllUsers() {
    return userState.stream().toList();
  }


}
