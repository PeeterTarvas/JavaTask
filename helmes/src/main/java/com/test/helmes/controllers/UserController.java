package com.test.helmes.controllers;


import com.test.helmes.dbos.UserDbo;
import com.test.helmes.dtos.UserDto;
import com.test.helmes.errors.InvalidDataException;
import com.test.helmes.services.UserService;
import org.hibernate.internal.build.AllowPrintStacktrace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        try {
            this.userService.register(userDto);
        } catch (InvalidDataException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Created: " + userDto.getUsername() + "\"}");
    }

}
