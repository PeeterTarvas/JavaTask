package com.test.helmes.controllers;


import com.test.helmes.controllers.helper.ResponseHandler;
import com.test.helmes.dtos.LoginResponseDto;
import com.test.helmes.dtos.UserDto;
import com.test.helmes.errors.Error;
import com.test.helmes.services.UserService;
import jakarta.validation.Valid;
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

    private final ResponseHandler responseHandler;

    @Autowired
    public UserController(UserService userService, ResponseHandler responseHandler) {
        this.userService = userService;
        this.responseHandler = responseHandler;
    }

    /**
     * This method is the endpoint that is called when the new user wants to register their account.
     * @param userDto that has the account details of the new user.
     * @return a response if the account creation is successful.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDto userDto) {
        try {
            this.userService.register(userDto);
            return responseHandler.returnResponse(HttpStatus.CREATED,
                    "{\"message\": \"Created: " + userDto.getUsername() + "\"}");
        } catch (Error error) {
            return responseHandler.returnErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY,
                    error);
        }
    }

    /**
     * This method enables the user to sign in to their account.
     * @param userDto with the users details of their account.
     * @return the login response if the login is successful, else return an error.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserDto userDto) {
        LoginResponseDto loginResponseDto =  userService.login(userDto);
        return responseHandler.returnResponse(HttpStatus.OK, loginResponseDto);
    }

}
