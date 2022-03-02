package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.controller.dto.CreateUserRequest;
import com.weareadaptive.auction.controller.dto.UpdateUserRequest;
import com.weareadaptive.auction.controller.dto.UserResponse;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {
  private final UserService userService;



  public UserController(UserService userService) {
    this.userService = userService;
  }


  @GetMapping
  ResponseEntity getAllUsers(){
    return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.getUserState().stream()
                    .toList());
  }

  @PostMapping
//  @ResponseStatus(HttpStatus.CREATED)
  ResponseEntity createUser(@RequestBody @Valid CreateUserRequest createUserRequest){

    try{
      var userName = createUserRequest.firstName();
      var firstName = createUserRequest.firstName();
      var lastName = createUserRequest.lastName();
      var passWord = createUserRequest.password();
      var organization = createUserRequest.organisation();

      var createdUser = userService.create(userName, passWord, firstName, lastName, organization);
      var userID = createdUser.getId();
      var userResponse = new UserResponse(userID, userName, firstName, lastName, organization);
      return ResponseEntity
              .status(HttpStatus.CREATED)
              .body(userResponse);

    } catch (RuntimeException e){
      var message = e.getMessage();
      return ResponseEntity
              .status(HttpStatus.BAD_REQUEST)
              .body(message);
    }




  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity findUserById(@PathVariable int id){

    var userOptional = userService.getUserState().stream()
            .filter(user -> user.getId() == id)
            .findFirst();
    if(userOptional.isPresent()){
      var foundUser = userOptional.get();
      return ResponseEntity
              .status(HttpStatus.OK)
              .body(foundUser);
    } else {
      return ResponseEntity
              .status(HttpStatus.NOT_FOUND)
              .body("");
    }

  }

  @PutMapping("{id}")
  ResponseEntity updateUser(@RequestBody @Valid UpdateUserRequest updateUserRequest, @PathVariable int id){
    var userOptional = userService.getUserState().stream()
            .filter(user -> user.getId() == id)
            .findFirst();

    if(userOptional.isPresent()){
      var foundUser = userOptional.get();
      foundUser.setFirstName(updateUserRequest.firstName());
      foundUser.setLastName(updateUserRequest.lastName());
      foundUser.setOrganisation(updateUserRequest.organisation());
      return ResponseEntity
              .status(HttpStatus.OK)
              .body(foundUser);
    } else {
      return ResponseEntity
              .status(HttpStatus.NOT_FOUND)
              .body("");
    }
  }

  @PutMapping("/{id}/block")
  ResponseEntity blockUser (@PathVariable int id){
    var foundUserOptional = findUserByID(id);
    if(foundUserOptional.isPresent()){
      foundUserOptional.get().block();
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
    }
  }

  @PutMapping("/{id}/unblock")
  ResponseEntity<String> unblockUser(@PathVariable int id){
    var foundUserOptional = findUserByID(id);
    if(foundUserOptional.isPresent()){
      foundUserOptional.get().unblock();
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
    }

  }


  private Optional<User> findUserByID(int id){
    return userService.getUserState().stream().filter(user -> user.getId() == id).findFirst();
  }

}
