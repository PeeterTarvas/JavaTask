package com.test.helmes.unitests.controllertests;

import com.test.helmes.controllers.company.CompanyController;
import com.test.helmes.dtos.company.CompanyDto;
import com.test.helmes.errors.Error;
import com.test.helmes.services.company.CompanyService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * This class contains unit tests for the {@link CompanyController} class.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {


    @MockBean
    private CompanyService companyService;

    @Autowired
    private CompanyController companyController;

    private CompanyDto companyDto;
    private String username;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        companyDto =  new CompanyDto("test", 6, true);
        username = "testUser";

    }

    /**
     * Test for saving a company with valid data in the application.
     */
    @Test
    public void testSaveCompanySuccess() throws Error {

        doNothing().when(companyService).saveCompany(username, companyDto);

        ResponseEntity<?> responseEntity = companyController.saveCompany(companyDto, username);

        verify(companyService, times(1)).saveCompany(username, companyDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    /**
     * Test saving a company with invalid data.
     */
    @Test
    public void testSaveCompanyInvalidDataException() throws Error {

        doThrow(new Error("Invalid data")).when(companyService).saveCompany(username, companyDto);

        ResponseEntity<?> responseEntity = companyController.saveCompany(companyDto, username);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    /**
     * Test updating a company with valid data.
     */
    @Test
    public void testUpdateCompanySuccess() throws Error {
        companyDto.setCompanyName("Test Company");

        doNothing().when(companyService).updateCompany(username, companyDto);

        ResponseEntity<?> responseEntity = companyController.updateCompany(companyDto, username);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    /**
     * Test updating a company with invalid data.
     */
    @Test
    public void testUpdateCompanyInvalidDataException() throws Error {

        doThrow(new Error("Invalid data")).when(companyService).updateCompany(username, companyDto);

        ResponseEntity<?> responseEntity = companyController.updateCompany(companyDto, username);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    /**
     * Test for getting a user's company when the company exists.
     */
    @Test
    public void testGetUsersCompanyExists() throws Error {
        companyDto.setCompanyName("Test Company");

        when(companyService.getUsersCompany(username)).thenReturn(Optional.of(companyDto));

        ResponseEntity<?> responseEntity = companyController.getUsersCompany(username);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        CompanyDto responseBody = (CompanyDto) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Test Company", responseBody.getCompanyName());
        assertEquals(1, responseBody.getCompanySectorId());
        assertTrue(responseBody.getCompanyTerms());
    }

    /**
     * Test for getting a user's company when the company does not exist. It still returns OK but the body is empty.
     */
    @Test
    public void testGetUsersCompanyNotFound() throws Error {

        when((companyService).getUsersCompany(username)).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = companyController.getUsersCompany(username);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testUserDtoHasMinimumAllowedSectorId() throws Error {
        companyDto.setCompanySectorId(1);

        doNothing().when(companyService).saveCompany(username, companyDto);

        ResponseEntity<?> responseEntity = companyController.saveCompany(companyDto, username);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testUserDtoHasMaximumAllowedSectorId() throws Error {
        companyDto.setCompanySectorId(80);

        doNothing().when(companyService).saveCompany(username, companyDto);

        ResponseEntity<?> responseEntity = companyController.saveCompany(companyDto, username);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testUserDtoHasOverMaximumAllowedSectorId() throws Error {
        companyDto.setCompanySectorId(81);

        doNothing().when(companyService).saveCompany(username, companyDto);

        ResponseEntity<?> responseEntity = companyController.saveCompany(companyDto, username);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testUserDtoHasNegativeOrZeroAsSectorId() throws Error {
        companyDto.setCompanySectorId(-1);

        doNothing().when(companyService).saveCompany(username, companyDto);

        assertThrows(ResponseEntity.getClass(), companyController.saveCompany(companyDto, username));


        companyDto.setCompanySectorId(0);

         companyController.saveCompany(companyDto, username);

        //assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());


    }

}
