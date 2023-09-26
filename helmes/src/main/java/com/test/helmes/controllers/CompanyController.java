package com.test.helmes.controllers;

import com.test.helmes.dtos.CompanyDto;
import com.test.helmes.errors.ErrorResponse;
import com.test.helmes.errors.InvalidDataException;
import com.test.helmes.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * This is the endpoint controller for all the methods that are related to the company objects logic.
 */
@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * Saves the company, if company doesn't have valid attributes it throws an error.
     * @param companyDto is the company object that the user wants to save or update.
     * @param username of the player that requests the update to be made.
     */
    @PostMapping("/{username}/save")
    public ResponseEntity<?> saveCompany(@RequestBody CompanyDto companyDto, @PathVariable String username) {
        try {
            companyService.saveCompany(username, companyDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("{\"message\": \"Created company: " + companyDto.getCompanyName() + "\"}");
        } catch (InvalidDataException e) {
            ErrorResponse errorResponse = new ErrorResponse("Invalid data", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
        }
    }

    /**
     * Endpoint that enables the user to update their company by calling a service that handles the updating.
     * @param companyDto that holds the updated details of the company.
     * @param username of the user that wants to update their company.
     * @return a response entity containing either an OK response or an error.
     */
    @PutMapping("/{username}/update")
    public ResponseEntity<?> updateCompany(@RequestBody CompanyDto companyDto, @PathVariable String username) {
        try {
            companyService.updateCompany(username, companyDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body("{\"message\": \"Updated company: " + companyDto.getCompanyName() + "\"}");
        } catch (InvalidDataException e) {
            ErrorResponse errorResponse = new ErrorResponse("Invalid data", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
        }
    }

    /**
     * This method returns the users company when the user logs-in.
     * @param username of the user who has logged-in.
     * @return the users company if it exists.
     */
    @GetMapping("/{username}/get")
    public ResponseEntity<?> getUsersCompany(@PathVariable String username) {
        Optional<CompanyDto> companyDto = companyService.getUsersCompany(username);
        if (companyDto.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(companyDto.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * @return all the company's in the database
     */
    @GetMapping("/getAll")
    public List<CompanyDto> getAll() {
        return companyService.getAll();
    }

}
