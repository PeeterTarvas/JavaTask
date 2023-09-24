package com.test.helmes.controllers;


import com.test.helmes.dtos.LoginResponseDto;
import com.test.helmes.dtos.UserDto;
import com.test.helmes.errors.InvalidDataException;
import com.test.helmes.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This is the endpoint controller for all user related logic.
 */
@RestController
@RequestMapping("/user")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * This method is the endpoint that is called when the new user wants to register their account.
     * @param userDto that has the account details of the new user.
     * @return a response if the account creation is successful.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        try {
            this.userService.register(userDto);
        } catch (InvalidDataException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Created: " + userDto.getUsername() + "\"}");
    }

    /**
     * This method enables the user to sign in to their account.
     * @param userDto with the users details of their account.
     * @return the login response if the login is successful, else return an error.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        try {
            LoginResponseDto loginResponseDto =  userService.login(userDto);
            return ResponseEntity.status(HttpStatus.OK).body(loginResponseDto);
        } catch (InvalidDataException e) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
