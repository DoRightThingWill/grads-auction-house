package com.weareadaptive.auction.controller.dto.response;


import com.weareadaptive.auction.model.User;

public record UserResponse(
    int id,
    String username,
    String firstName,
    String lastName,
    String organisation
) {

  // may use a mapper class
  public UserResponse(User user) {
    this(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(),
        user.getOrganisation());
  }

}

