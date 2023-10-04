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

/**
 * This tests ConverterService that tests different methods for mapping dtos to dbos and vice versa.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ConverterServiceTest {

    @Autowired
    private ConverterService converterService;

    /**
     * Tests converting CompanyDto to CompanyDbo.
     */
    @Test
    public void testConvertToCompanyDbo() {
        CompanyDto companyDto = new CompanyDto("TestCompany", 1, true);

        CompanyDbo companyDbo = converterService.convertToCompanyDbo(companyDto);

        assertEquals(companyDto.getCompanyName(), companyDbo.getCompanyName());
        assertEquals(companyDto.getCompanySectorId(), companyDbo.getCompanySectorId());
        assertEquals(companyDto.getCompanyTerms(), companyDbo.getCompanyTerms());
    }

    /**
     * Tests converting CompanyDbo to CompanyDto.
     */
    @Test
    public void testConvertToCompanyDto() {
        CompanyDbo companyDbo = new CompanyDbo(1L, "TestCompany", 1, true);

        CompanyDto companyDto = converterService.convertToCompanyDto(companyDbo);

        assertEquals(companyDbo.getCompanyName(), companyDto.getCompanyName());
        assertEquals(companyDbo.getCompanySectorId(), companyDto.getCompanySectorId());
        assertEquals(companyDbo.getCompanyTerms(), companyDto.getCompanyTerms());
    }

    /**
     * Tests converting SectorDbo to SectorDto.
     */
    @Test
    public void testConvertToSectorDto() {
        SectorDbo sectorDbo = new SectorDbo(1,"TestSector", 2);

        SectorDto sectorDto = converterService.convertToSectorDto(sectorDbo);

        assertEquals(sectorDbo.getSectorName(), sectorDto.getSectorName());
        assertEquals(sectorDbo.getSectorId(), sectorDto.getSectorId());
        assertEquals(sectorDbo.getSectorParentId(), sectorDto.getSectorParentId());
    }

    /**
     * Tests converting UserDto to UserDbo.
     */
    @Test
    public void testConvertToUserDbo() {
        UserDto userDto = new UserDto("testUser", "password", null);

        UserDbo userDbo = converterService.convertToUserDbo(userDto);

        assertEquals(userDto.getUsername(), userDbo.getUsername());
        assertEquals(userDto.getPassword(), userDbo.getPassword());
    }

    /**
     * Tests converting UserDbo to userDto.
     */
    @Test
    public void testConvertToUserDto() {
        UserDbo userDbo = UserDbo.builder()
                .username("testUser")
                .password("password")
                .build();

        UserDto userDto = converterService.convertToUserDto(userDbo);

        assertEquals(userDbo.getUsername(), userDto.getUsername());
        assertEquals(userDbo.getPassword(), userDto.getPassword());
    }

    /**
     * Tests creating CompanyReferenceDbo from UserDbo and CompanyDbo.
     */
    @Test
    public void testCreateUserCompanyReferenceDbo() {
        UserDbo userDbo = UserDbo.builder()
                .username("testUser")
                .password("password")
                .build();
        CompanyDbo companyDbo = CompanyDbo.builder()
                .companyName("TestCompany")
                .companySectorId(1)
                .companyTerms(true)
                .build();

        UserCompanyReferenceDbo userCompanyReferenceDbo = converterService.createUserCompanyReferenceDbo(userDbo, companyDbo);

        assertEquals(userDbo, userCompanyReferenceDbo.getUserReference());
        assertEquals(companyDbo, userCompanyReferenceDbo.getCompanyReference());
    }

}
