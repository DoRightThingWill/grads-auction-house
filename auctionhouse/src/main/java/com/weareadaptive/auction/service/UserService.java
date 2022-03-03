package com.weareadaptive.auction.service;

import com.weareadaptive.auction.exception.ModelNotFoundException;
import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.model.UserState;
import org.springframework.stereotype.Service;

import java.net.PortUnreachableException;
import java.util.List;

@Service
public class UserService {
  private final UserState userState;

  public UserService(UserState userState) {
    this.userState = userState;
  }



  public User create(String username, String password, String firstName, String lastName,
                     String organisation) {

    if(userState.getByUsername(username).isPresent()){
      throw new BusinessException("username already exist");
    }

    var userID = userState.nextId();
    var user = new User(userID, username, password, firstName, lastName, organisation);
    userState.add(user);

    return user;
//    throw new UnsupportedOperationException();
  }

  public User getUserByID(int id){
    return userState.getUserByID(id)
            .orElseThrow(ModelNotFoundException::new);
  }

  public List<User> getAllUsers(){
    return userState.stream().toList();
  }


}
