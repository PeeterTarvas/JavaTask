package com.test.helmes.servicetests;

import com.test.helmes.dbos.CompanyDbo;
import com.test.helmes.dbos.SectorDbo;
import com.test.helmes.dbos.UserCompanyReferenceDbo;
import com.test.helmes.dbos.UserDbo;
import com.test.helmes.dtos.CompanyDto;
import com.test.helmes.dtos.SectorDto;
import com.test.helmes.dtos.UserDto;
import com.test.helmes.services.ConverterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureMockMvc
public class ConverterServiceTest {

    @Autowired
    private ConverterService converterService;

    @Test
    public void testConvertToCompanyDbo() {
        // Arrange
        CompanyDto companyDto = new CompanyDto("TestCompany", 1, true);

        // Act
        CompanyDbo companyDbo = converterService.convertToCompanyDbo(companyDto);

        // Assert
        assertEquals(companyDto.getCompanyName(), companyDbo.getCompanyName());
        assertEquals(companyDto.getCompanySectorId(), companyDbo.getCompanySectorId());
        assertEquals(companyDto.getCompanyTerms(), companyDbo.getCompanyTerms());
    }

    @Test
    public void testConvertToCompanyDto() {
        // Arrange
        CompanyDbo companyDbo = new CompanyDbo(1L, "TestCompany", 1, true);

        // Act
        CompanyDto companyDto = converterService.convertToCompanyDto(companyDbo);

        // Assert
        assertEquals(companyDbo.getCompanyName(), companyDto.getCompanyName());
        assertEquals(companyDbo.getCompanySectorId(), companyDto.getCompanySectorId());
        assertEquals(companyDbo.getCompanyTerms(), companyDto.getCompanyTerms());
    }

    @Test
    public void testConvertToSectorDto() {
        // Arrange
        SectorDbo sectorDbo = new SectorDbo(1,"TestSector", 2);

        // Act
        SectorDto sectorDto = converterService.convertToSectorDto(sectorDbo);

        // Assert
        assertEquals(sectorDbo.getSectorName(), sectorDto.getSectorName());
        assertEquals(sectorDbo.getSectorId(), sectorDto.getSectorId());
        assertEquals(sectorDbo.getSectorParentId(), sectorDto.getSectorParentId());
    }

    @Test
    public void testConvertToUserDbo() {
        // Arrange
        UserDto userDto = new UserDto("testUser", "password", null);

        // Act
        UserDbo userDbo = converterService.convertToUserDbo(userDto);

        // Assert
        assertEquals(userDto.getUsername(), userDbo.getUsername());
        assertEquals(userDto.getPassword(), userDbo.getPassword());
    }

    @Test
    public void testConvertToUserDto() {
        // Arrange
        UserDbo userDbo = UserDbo.builder()
                .username("testUser")
                .password("password")
                .build();

        // Act
        UserDto userDto = converterService.convertToUserDto(userDbo);

        // Assert
        assertEquals(userDbo.getUsername(), userDto.getUsername());
        assertEquals(userDbo.getPassword(), userDto.getPassword());
    }

    @Test
    public void testCreateUserCompanyReferenceDbo() {
        // Arrange
        UserDbo userDbo = UserDbo.builder()
                .username("testUser")
                .password("password")
                .build();
        CompanyDbo companyDbo = CompanyDbo.builder()
                .companyName("TestCompany")
                .companySectorId(1)
                .companyTerms(true)
                .build();

        // Act
        UserCompanyReferenceDbo userCompanyReferenceDbo = converterService.createUserCompanyReferenceDbo(userDbo, companyDbo);

        // Assert
        assertEquals(userDbo, userCompanyReferenceDbo.getUserReference());
        assertEquals(companyDbo, userCompanyReferenceDbo.getCompanyReference());
    }

}
