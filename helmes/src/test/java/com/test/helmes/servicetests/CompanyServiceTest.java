package com.test.helmes.servicetests;

import com.test.helmes.controllers.UserController;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    public void testSaveCompanySuccess() {
        // Arrange
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

        // Act
        assertDoesNotThrow(() -> companyService.saveCompany(username, companyDto));

        // Assert
        verify(userRepository, times(1)).getUserDboByUsername(username);
        verify(converterService, times(1)).convertToCompanyDbo(companyDto);
        verify(userCompanyReferenceRepository, times(1)).save(any(UserCompanyReferenceDbo.class));
    }

    @Test
    public void testSaveCompanyInvalidDataUserNotFound() {
        // Arrange
        String username = "testUser";
        CompanyDto companyDto = new CompanyDto("testCompany", 1, true);

        when(userRepository.getUserDboByUsername(username)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(InvalidDataException.class, () -> companyService.saveCompany(username, companyDto));

        // Verify that the methods were not called
        verify(userRepository, times(1)).getUserDboByUsername(username);
        verify(converterService, never()).convertToCompanyDbo(companyDto);
        verify(userCompanyReferenceRepository, never()).save(any(UserCompanyReferenceDbo.class));
    }

    @Test
    public void testUpdateCompanySuccess() {
        // Arrange
        String username = "testUser";
        CompanyDto companyDto = new CompanyDto("updatedCompany", 2, true);
        CompanyDbo companyDbo = new CompanyDbo();
        UserDbo userDbo = new UserDbo(); // Create a userDbo instance
        UserCompanyReferenceDbo userCompanyReferenceDbo = new UserCompanyReferenceDbo(1L, userDbo, companyDbo);

        when(userRepository.getUserDboByUsername(username)).thenReturn(Optional.of(userDbo));
        doReturn(Optional.of(userCompanyReferenceDbo)).when(userCompanyReferenceRepository)
                .getUserCompanyReferenceDboByUserReference(userDbo);

        // Act
        assertDoesNotThrow(() -> companyService.updateCompany(username, companyDto));

        // Assert
        verify(userRepository, times(1)).getUserDboByUsername(username);
        verify(userCompanyReferenceRepository, times(1)).getUserCompanyReferenceDboByUserReference(userDbo);
        verify(companyRepository, times(1)).save(any(CompanyDbo.class));
    }

    @Test
    public void testUpdateCompanyInvalidDataUserNotFound() {
        // Arrange
        String username = "testUser";
        CompanyDto companyDto = new CompanyDto("updatedCompany", 2, false);

        when(userRepository.getUserDboByUsername(username)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(InvalidDataException.class, () -> companyService.updateCompany(username, companyDto));

        // Verify that the methods were not called
        verify(userRepository, times(1)).getUserDboByUsername(username);
        verify(userCompanyReferenceRepository, never()).getUserCompanyReferenceDboByUserReference(any(UserDbo.class));
        verify(companyRepository, never()).save(any(CompanyDbo.class));
    }

    @Test
    public void testUpdateCompanyInvalidDataUserHasNoCompany() {
        // Arrange
        String username = "testUser";
        CompanyDto companyDto = new CompanyDto("updatedCompany", 2, false);
        UserDbo userDbo = new UserDbo(); // Create a userDbo instance

        when(userRepository.getUserDboByUsername(username)).thenReturn(Optional.of(userDbo));

        // Act and Assert
        assertThrows(InvalidDataException.class, () -> companyService.updateCompany(username, companyDto));

        // Verify that the methods were not called
        verify(userRepository, times(1)).getUserDboByUsername(username);
        verify(companyRepository, never()).save(any(CompanyDbo.class));
    }

    @Test
    public void testGetUsersCompanySuccess() {
        // Arrange
        String username = "testUser";
        UserDbo userDbo = new UserDbo(); // Create a userDbo instance
        CompanyDbo companyDbo = new CompanyDbo(); // Create a companyDbo instance
        UserCompanyReferenceDbo userCompanyReferenceDbo = new UserCompanyReferenceDbo();
        userCompanyReferenceDbo.setCompanyReference(companyDbo);
        CompanyDto companyDto = new CompanyDto("Test Company", 1, true);

        when(userRepository.getUserDboByUsername(username)).thenReturn(Optional.of(userDbo));
        when(userCompanyReferenceRepository.getUserCompanyReferenceDboByUserReference(userDbo))
                .thenReturn(Optional.of(userCompanyReferenceDbo));
        when(converterService.convertToCompanyDto(companyDbo)).thenReturn(companyDto);

        // Act
        Optional<CompanyDto> result = companyService.getUsersCompany(username);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(companyDto.getCompanyName(), result.get().getCompanyName());
        assertEquals(companyDto.getCompanySectorId(), result.get().getCompanySectorId());
        assertEquals(companyDto.getCompanyTerms(), result.get().getCompanyTerms());
    }

    @Test
    public void testGetUsersCompanyUserNotFound() {
        // Arrange
        String username = "testUser";

        when(userRepository.getUserDboByUsername(username)).thenReturn(Optional.empty());

        // Act
        Optional<CompanyDto> result = companyService.getUsersCompany(username);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetUsersCompanyUserHasNoCompany() {
        // Arrange
        String username = "testUser";
        UserDbo userDbo = new UserDbo(); // Create a userDbo instance

        when(userRepository.getUserDboByUsername(username)).thenReturn(Optional.of(userDbo));
        when(userCompanyReferenceRepository.getUserCompanyReferenceDboByUserReference(userDbo))
                .thenReturn(Optional.empty());

        // Act
        Optional<CompanyDto> result = companyService.getUsersCompany(username);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    public void testIsValid_ValidData() {
        // Arrange
        CompanyDto companyDto = new CompanyDto("Test Company", 1, true);

        // Act
        boolean result = companyService.isValid(companyDto);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsValid_InvalidName() {
        // Arrange
        CompanyDto companyDto = new CompanyDto("", 1, true);

        // Act
        boolean result = companyService.isValid(companyDto);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsValid_NullSectorId() {
        // Arrange
        CompanyDto companyDto = new CompanyDto("Test Company", null, true);

        // Act
        boolean result = companyService.isValid(companyDto);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsValid_InvalidTerms() {
        // Arrange
        CompanyDto companyDto = new CompanyDto("Test Company", 1, false);

        // Act
        boolean result = companyService.isValid(companyDto);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsValidName_ValidName() {
        // Arrange
        String name = "Test Company";

        // Act
        boolean result = companyService.isValidName(name);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsValidName_EmptyName() {
        // Arrange
        String name = "";

        // Act
        boolean result = companyService.isValidName(name);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsValidName_NullName() {
        // Arrange
        String name = null;

        // Act
        boolean result = companyService.isValidName(name);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsValidName_WhitespaceName() {
        // Arrange
        String name = "   ";

        // Act
        boolean result = companyService.isValidName(name);

        // Assert
        assertFalse(result);
    }

}
