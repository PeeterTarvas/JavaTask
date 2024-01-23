package com.test.helmes.servicetests;

import com.test.helmes.dbos.CompanyDbo;
import com.test.helmes.dbos.UserCompanyReferenceDbo;
import com.test.helmes.dbos.UserDbo;
import com.test.helmes.dtos.CompanyDto;
import com.test.helmes.errors.InvalidDataException;
import com.test.helmes.repositories.CompanyRepository;
import com.test.helmes.repositories.UserCompanyReferenceRepository;
import com.test.helmes.repositories.UserRepository;
import com.test.helmes.services.CompanyService;
import com.test.helmes.services.ConverterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * These tests are for testing methods inside CompanyService class.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CompanyServiceTest {

    @MockBean
    private CompanyRepository companyRepository;

    @MockBean
    private ConverterService converterService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserCompanyReferenceRepository userCompanyReferenceRepository;

    @Autowired
    private CompanyService companyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    /**
     * This tests the saving of a company, should be successful.
     */
    @Test
    public void testSaveCompanySuccess() {
        String username = "testUser";
        CompanyDto companyDto = new CompanyDto("testCompany", 1, true);
        UserDbo userDbo = new UserDbo(1L, "test", "pass"); // Create a userDbo instance
        CompanyDbo companyDbo = new CompanyDbo(1L, "testCompany", 1, true);
        UserCompanyReferenceDbo userCompanyReferenceDbo = new UserCompanyReferenceDbo(1L, userDbo, companyDbo);

        when(userRepository.getUserDboByUsername(username)).thenReturn(Optional.of(userDbo));
        when(converterService.convertToCompanyDbo(companyDto)).thenReturn(companyDbo);
        when(converterService.createUserCompanyReferenceDbo(userDbo, companyDbo)).thenReturn(userCompanyReferenceDbo);
        when(companyRepository.getCompanyDboByCompanyName("testCompany")).thenReturn(Optional.of(companyDbo));
        when(userCompanyReferenceRepository.save(userCompanyReferenceDbo)).thenReturn(userCompanyReferenceDbo);

        assertDoesNotThrow(() -> companyService.saveCompany(username, companyDto));

        verify(userRepository, times(1)).getUserDboByUsername(username);
        verify(converterService, times(1)).convertToCompanyDbo(companyDto);
        verify(userCompanyReferenceRepository, times(1)).save(any(UserCompanyReferenceDbo.class));
    }

    /**
     * Test saving a company with not an acceptable user.
     */
    @Test
    public void testSaveCompanyInvalidDataUserNotFound() {
        String username = "testUser";
        CompanyDto companyDto = new CompanyDto("testCompany", 1, true);

        when(userRepository.getUserDboByUsername(username)).thenReturn(Optional.empty());

        assertThrows(InvalidDataException.class, () -> companyService.saveCompany(username, companyDto));

        verify(userRepository, times(1)).getUserDboByUsername(username);
        verify(converterService, never()).convertToCompanyDbo(companyDto);
        verify(userCompanyReferenceRepository, never()).save(any(UserCompanyReferenceDbo.class));
    }

    /**
     * Test updating a company that is successful.
     */
    @Test
    public void testUpdateCompanySuccess() {
        String username = "testUser";
        CompanyDto companyDto = new CompanyDto("updatedCompany", 2, true);
        CompanyDbo companyDbo = new CompanyDbo();
        UserDbo userDbo = new UserDbo();
        UserCompanyReferenceDbo userCompanyReferenceDbo = new UserCompanyReferenceDbo(1L, userDbo, companyDbo);

        when(userRepository.getUserDboByUsername(username)).thenReturn(Optional.of(userDbo));
        doReturn(Optional.of(userCompanyReferenceDbo)).when(userCompanyReferenceRepository)
                .getUserCompanyReferenceDboByUserReference(userDbo);

        assertDoesNotThrow(() -> companyService.updateCompany(username, companyDto));

        verify(userRepository, times(1)).getUserDboByUsername(username);
        verify(userCompanyReferenceRepository, times(1)).getUserCompanyReferenceDboByUserReference(userDbo);
        verify(companyRepository, times(1)).save(any(CompanyDbo.class));
    }

    /**
     * This tests updating a company with invalid user.
     */
    @Test
    public void testUpdateCompanyInvalidDataUserNotFound() {
        String username = "testUser";
        CompanyDto companyDto = new CompanyDto("updatedCompany", 2, false);

        when(userRepository.getUserDboByUsername(username)).thenReturn(Optional.empty());

        assertThrows(InvalidDataException.class, () -> companyService.updateCompany(username, companyDto));

        verify(userRepository, times(1)).getUserDboByUsername(username);
        verify(userCompanyReferenceRepository, never()).getUserCompanyReferenceDboByUserReference(any(UserDbo.class));
        verify(companyRepository, never()).save(any(CompanyDbo.class));
    }

    /**
     *  Test updating a company with a user that has no company.
     */
    @Test
    public void testUpdateCompanyInvalidDataUserHasNoCompany() {
        String username = "testUser";
        CompanyDto companyDto = new CompanyDto("updatedCompany", 2, false);
        UserDbo userDbo = new UserDbo();

        when(userRepository.getUserDboByUsername(username)).thenReturn(Optional.of(userDbo));

        assertThrows(InvalidDataException.class, () -> companyService.updateCompany(username, companyDto));

        verify(userRepository, times(1)).getUserDboByUsername(username);
        verify(companyRepository, never()).save(any(CompanyDbo.class));
    }

    /**
     * This method tests getting users company successfully.
     */
    @Test
    public void testGetUsersCompanySuccess() {
        String username = "testUser";
        UserDbo userDbo = new UserDbo();
        CompanyDbo companyDbo = new CompanyDbo();
        UserCompanyReferenceDbo userCompanyReferenceDbo = new UserCompanyReferenceDbo();
        userCompanyReferenceDbo.setCompanyReference(companyDbo);
        CompanyDto companyDto = new CompanyDto("Test Company", 1, true);

        when(userRepository.getUserDboByUsername(username)).thenReturn(Optional.of(userDbo));
        when(userCompanyReferenceRepository.getUserCompanyReferenceDboByUserReference(userDbo))
                .thenReturn(Optional.of(userCompanyReferenceDbo));
        when(converterService.convertToCompanyDto(companyDbo)).thenReturn(companyDto);

        Optional<CompanyDto> result = companyService.getUsersCompany(username);

        assertTrue(result.isPresent());
        assertEquals(companyDto.getCompanyName(), result.get().getCompanyName());
        assertEquals(companyDto.getCompanySectorId(), result.get().getCompanySectorId());
        assertEquals(companyDto.getCompanyTerms(), result.get().getCompanyTerms());
    }

    /**
     * This method tests getting a users company when user is not found.
     */
    @Test
    public void testGetUsersCompanyUserNotFound() {
        String username = "testUser";

        when(userRepository.getUserDboByUsername(username)).thenReturn(Optional.empty());

        Optional<CompanyDto> result = companyService.getUsersCompany(username);

        assertFalse(result.isPresent());
    }

    /**
     * Tests getting a users company when user has no company.
     */
    @Test
    public void testGetUsersCompanyUserHasNoCompany() {
        String username = "testUser";
        UserDbo userDbo = new UserDbo();

        when(userRepository.getUserDboByUsername(username)).thenReturn(Optional.of(userDbo));
        when(userCompanyReferenceRepository.getUserCompanyReferenceDboByUserReference(userDbo))
                .thenReturn(Optional.empty());

        Optional<CompanyDto> result = companyService.getUsersCompany(username);

        assertFalse(result.isPresent());
    }

    /**
     * Tests isValid method that controls company dto validity, this tests a valid company.
     */
    @Test
    public void testIsValidValidData() {
        CompanyDto companyDto = new CompanyDto("Test Company", 1, true);


        boolean result = companyService.isValid(companyDto);

        assertTrue(result);
    }

    /**
     * This tests isValid method but with invalid company name.
     */
    @Test
    public void testIsValidInvalidName() {
        CompanyDto companyDto = new CompanyDto("", 1, true);

        boolean result = companyService.isValid(companyDto);

        assertFalse(result);
    }

    /**
     * Test isValid method when company does not have sectorId.
     */
    @Test
    public void testIsValidNullSectorId() {
        CompanyDto companyDto = new CompanyDto("Test Company", null, true);

        boolean result = companyService.isValid(companyDto);

        assertFalse(result);
    }

    /**
     * Tests isValid when company has invalid terms.
     */
    @Test
    public void testIsValidInvalidTerms() {
        CompanyDto companyDto = new CompanyDto("Test Company", 1, false);

        boolean result = companyService.isValid(companyDto);

        assertFalse(result);
    }

    /**
     * Tests method isValidName when name is valid.
     */
    @Test
    public void testIsValidNameValidName() {
        String name = "Test Company";

        boolean result = companyService.isValidName(name);

        assertTrue(result);
    }

    /**
     * Tests isValidName when name is empty.
     */
    @Test
    public void testIsValidNameEmptyName() {
        String name = "";

        boolean result = companyService.isValidName(name);

        assertFalse(result);
    }

    /**
     * Tests isValidName when name is null.
     */
    @Test
    public void testIsValidNameNullName() {
        String name = null;

        boolean result = companyService.isValidName(name);

        assertFalse(result);
    }

    /**
     * Tests when name is a whitespace.
     */
    @Test
    public void testIsValidNameWhitespaceName() {
        String name = "   ";

        boolean result = companyService.isValidName(name);

        assertFalse(result);
    }

}
