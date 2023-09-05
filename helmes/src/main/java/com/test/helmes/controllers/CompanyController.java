package com.test.helmes.controllers;

import com.test.helmes.dtos.CompanyDto;
import com.test.helmes.errors.ErrorResponse;
import com.test.helmes.errors.InvalidDataException;
import com.test.helmes.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PutMapping("/save")
    public ResponseEntity<?> saveCompany(@RequestBody CompanyDto companyDto) {
        try {
            companyService.saveCompany(companyDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (InvalidDataException e) {
            ErrorResponse errorResponse = new ErrorResponse("Invalid data", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);        }
    }

}
