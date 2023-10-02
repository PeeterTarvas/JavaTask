package com.test.helmes.controllertests;


import com.test.helmes.controllers.CompanyController;
import com.test.helmes.dtos.CompanyDto;
import com.test.helmes.errors.InvalidDataException;
import com.test.helmes.services.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.BDDMockito.given;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {


    @MockBean
    private CompanyService companyService;

    @Autowired
    private CompanyController companyController;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveCompanySuccess() throws InvalidDataException {
        CompanyDto companyDto = new CompanyDto("test", 1, true);
        String username = "testUser";

        doNothing().when(companyService).saveCompany(username, companyDto);

        ResponseEntity<?> responseEntity = companyController.saveCompany(companyDto, username);

        verify(companyService, times(1)).saveCompany(username, companyDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void testSaveCompanyInvalidDataException() throws InvalidDataException {
        CompanyDto companyDto = new CompanyDto("test", 1, true);
        String username = "testUser";

        doThrow(new InvalidDataException("Invalid data")).when(companyService).saveCompany(username, companyDto);

        ResponseEntity<?> responseEntity = companyController.saveCompany(companyDto, username);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateCompanySuccess() throws InvalidDataException {
        CompanyDto companyDto = new CompanyDto("test", 1, true);
        companyDto.setCompanyName("Test Company");
        String username = "testUser";

        doNothing().when(companyService).updateCompany(username, companyDto);

        ResponseEntity<?> responseEntity = companyController.updateCompany(companyDto, username);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateCompanyInvalidDataException() throws InvalidDataException {
        CompanyDto companyDto = new CompanyDto("test", 1, true);
        String username = "testUser";

        doThrow(new InvalidDataException("Invalid data")).when(companyService).updateCompany(username, companyDto);

        ResponseEntity<?> responseEntity = companyController.updateCompany(companyDto, username);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    public void testGetUsersCompanyExists() {
        String username = "testUser";
        CompanyDto companyDto = new CompanyDto("test", 1, true);
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

    @Test
    public void testGetUsersCompanyNotFound() {
        String username = "testUser";

        when((companyService).getUsersCompany(username)).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = companyController.getUsersCompany(username);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }


}
