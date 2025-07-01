package com.irisandco.ecommerce_optic.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService USER_SERVICE;

    public UserController(UserService userService){
        USER_SERVICE = userService;
    }

    @GetMapping("")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok(USER_SERVICE.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        UserResponse userResponse = USER_SERVICE.getUserResponseById(id);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<UserResponse> createUser(@Validated @RequestBody UserRequest userRequest){
        UserResponse userResponse = USER_SERVICE.saveUser(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Validated @RequestBody UserRequest userRequest){
        UserResponse userResponse = USER_SERVICE.updateUser(id, userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Object> deleteUser(@PathVariable Long id){
        USER_SERVICE.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
