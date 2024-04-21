package com.test.helmes.controllers.company;

import com.test.helmes.controllers.helper.ResponseHandler;
import com.test.helmes.dtos.company.CompanyDto;
import com.test.helmes.errors.Error;
import com.test.helmes.services.company.CompanyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * This is the endpoint controller for all the methods that are related to the company objects logic.
 */
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    private final ResponseHandler responseHandler;

    /**
     * Saves the company, if company doesn't have valid attributes it throws an error.
     * @param companyDto is the company object that the user wants to save or update.
     * @param username of the player that requests the update to be made.
     */
    @PostMapping("/{username}/save")
    public ResponseEntity<?> saveCompany(@Valid @RequestBody CompanyDto companyDto, @PathVariable @NotBlank String username) {
        try {
            log.info("Saving company: " + companyDto.toString() + "for user: " + username);
            companyService.saveCompany(username, companyDto);
            log.info("Saved company: " + companyDto.toString() + "for user: " + username);
            return responseHandler.returnResponse(HttpStatus.CREATED,
                    "{\"message\": \"Created company: " + companyDto.getCompanyName() + "\"}");
        } catch (Error error) {
            log.error("Error when saving a company: " + companyDto.toString() + "for user: "
                    + username + " with error " + error.getMessage());
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
            log.info("Update company: " + companyDto.toString() + "for user: " + username);
            companyService.updateCompany(username, companyDto);
            log.info("Updated company: " + companyDto.toString() + "for user: " + username);
            return responseHandler.returnResponse(HttpStatus.OK,
                    "{\"message\": \"Updated company: " + companyDto.getCompanyName() + "\"}");
        } catch (Error error) {
            log.error("Error when updating a company: " + companyDto.toString() + "for user: "
                    + username + " with error " + error.getMessage());
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
            log.info("Get company " + "for user: " + username);
            Optional<CompanyDto> companyDto = companyService.getUsersCompany(username);
            if (companyDto.isPresent()) {
                log.info("Return company: " + companyDto.get().toString() + "for user: " + username);
                return responseHandler.returnResponse(HttpStatus.OK,
                        companyDto.get());
            }
        } catch (Error ignored) {
            log.error("Error when getting a company " + "for user: " + username + " with error: " + ignored.getMessage());
        }
        return responseHandler.returnResponse(HttpStatus.OK);}

}
