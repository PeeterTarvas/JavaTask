package com.test.helmes.controllers;

import com.test.helmes.controllers.helper.ResponseHandler;
import com.test.helmes.dtos.CompanyDto;
import com.test.helmes.errors.Error;
import com.test.helmes.services.CompanyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * This is the endpoint controller for all the methods that are related to the company objects logic.
 */
@Validated
@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    private final ResponseHandler responseHandler;

    @Autowired
    public CompanyController(CompanyService companyService, ResponseHandler responseHandler) {
        this.companyService = companyService;
        this.responseHandler = responseHandler;
    }

    /**
     * Saves the company, if company doesn't have valid attributes it throws an error.
     * @param companyDto is the company object that the user wants to save or update.
     * @param username of the player that requests the update to be made.
     */
    @PostMapping("/{username}/save")
    public ResponseEntity<?> saveCompany(@Valid @RequestBody CompanyDto companyDto, @PathVariable @NotBlank String username) {
        try {
            companyService.saveCompany(username, companyDto);
            return responseHandler.returnResponse(HttpStatus.CREATED,
                    "{\"message\": \"Created company: " + companyDto.getCompanyName() + "\"}");
        } catch (Error error) {
            return responseHandler.returnErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, error);
        }
    }

    /**
     * Endpoint that enables the user to update their company by calling a service that handles the updating.
     * @param companyDto that holds the updated details of the company.
     * @param username of the user that wants to update their company.
     * @return a response entity containing either an OK response or an error.
     */
    @PutMapping("/{username}/update")
    public ResponseEntity<?> updateCompany(@Valid @RequestBody CompanyDto companyDto, @PathVariable @NotBlank String username) {
        try {
            companyService.updateCompany(username, companyDto);
            return responseHandler.returnResponse(HttpStatus.OK,
                    "{\"message\": \"Updated company: " + companyDto.getCompanyName() + "\"}");
        } catch (Error error) {
            return responseHandler.returnErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, error);
        }
    }

    /**
     * This method returns the users company when the user logs-in.
     * @param username of the user who has logged-in.
     * @return the users company if it exists.
     */
    @GetMapping("/{username}/get")
    public ResponseEntity<?> getUsersCompany(@PathVariable @NotBlank String username) {
        try {
            Optional<CompanyDto> companyDto = companyService.getUsersCompany(username);
            if (companyDto.isPresent()) {
                return responseHandler.returnResponse(HttpStatus.OK,
                        companyDto.get());
            }
        } catch (Error ignored) {}
        return responseHandler.returnResponse(HttpStatus.OK);}

}
