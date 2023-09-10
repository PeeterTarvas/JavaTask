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

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveCompany(@RequestBody CompanyDto companyDto) {
        ResponseEntity<?> resp;
        try {
          companyService.saveCompany(companyDto);
          resp = ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Created: " + companyDto.getCompanyName() + "\"}");;
          return resp;
        } catch (InvalidDataException e) {
            ErrorResponse errorResponse = new ErrorResponse("Invalid data", e.getMessage());
            resp = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
            return resp;
        }
    }
    @GetMapping("/getAll")
    public List<CompanyDto> getAll() {
        return companyService.getAll();
    }

}
