package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.controller.dto.request.CreateUserRequest;
import com.weareadaptive.auction.controller.dto.request.UpdateUserRequest;
import com.weareadaptive.auction.controller.dto.response.UserResponse;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.service.UserService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  List<User> getAllUsers() {
    return userService.getAllUsers();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  UserResponse create(@RequestBody @Valid CreateUserRequest createUserRequest) {
    var createdUser = userService.create(
       createUserRequest.username(),
       createUserRequest.password(),
       createUserRequest.firstName(),
       createUserRequest.lastName(),
       createUserRequest.organisation());
    return new UserResponse(createdUser);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  UserResponse findUserById(@PathVariable int id) {
    return new UserResponse(userService.getUserByID(id));
  }

  @PutMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  UserResponse updateUser(
    @RequestBody @Valid UpdateUserRequest updateUserRequest,
    @PathVariable int id) {
    User foundUser = userService.getUserByID(id);
    foundUser.setFirstName(updateUserRequest.firstName());
    foundUser.setLastName(updateUserRequest.lastName());
    foundUser.setOrganisation(updateUserRequest.organisation());
    return new UserResponse(foundUser);
  }

  @PutMapping("/{id}/block")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void blockUser(@PathVariable int id) {
    User foundUser = userService.getUserByID(id);
    foundUser.block();
  }

  @PutMapping("/{id}/unblock")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void unblockUser(@PathVariable int id) {
    User foundUser = userService.getUserByID(id);
    foundUser.unblock();
  }

}
