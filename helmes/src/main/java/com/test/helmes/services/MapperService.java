package com.test.helmes.services;

import com.test.helmes.dbos.CompanyDbo;
import com.test.helmes.dbos.SectorDbo;
import com.test.helmes.dbos.UserCompanyReferenceDbo;
import com.test.helmes.dbos.UserDbo;
import com.test.helmes.dtos.CompanyDto;
import com.test.helmes.dtos.SectorDto;
import com.test.helmes.dtos.UserDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

/**
 * This class is for conversion methods, i.e. convert from dto to dbo or vice versa.
 */

@Service
public class MapperService {

    /**
     * Convert from CompanyDto to CompanyDbo.
     */
    public CompanyDbo convertToCompanyDbo(@Valid CompanyDto companyDto) {
        return CompanyDbo.builder()
                .companyName(companyDto.getCompanyName())
                .companySectorId(companyDto.getCompanySectorId())
                .companyTerms(companyDto.getCompanyTerms())
                .build();
    }

    /**
     * Convert from CompanyDbo to CompanyDto.
     */
    public CompanyDto convertToCompanyDto(@NotNull CompanyDbo companyDbo) {
        return CompanyDto.builder()
                .companyName(companyDbo.getCompanyName())
                .companySectorId(companyDbo.getCompanySectorId())
                .companyTerms(companyDbo.getCompanyTerms())
                .build();
    }

    /**
     * Convert SectorDbo to SectorDto.
     */
    public SectorDto convertToSectorDto(@NotNull SectorDbo sectorDbo) {
        return SectorDto.builder()
                .sectorName(sectorDbo.getSectorName())
                .sectorId(sectorDbo.getSectorId())
                .sectorParentId(sectorDbo.getSectorParentId())
                .build();
    }


    /**
     * Convert from UserDto to UserDbo.
     */
    public UserDbo convertToUserDbo(@Valid UserDto userDto) {
        return UserDbo.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .build();
    }

    /**
     * Convert from UserDbo to UserDto.
     */
    public UserDto convertToUserDto(@NotNull UserDbo userDbo) {
        return new UserDto(userDbo.getUsername(), userDbo.getPassword(), null);
    }

    /**
     * Build UserCompanyReferenceDbo from UserDbo and CompanyDbo.
     */
    public UserCompanyReferenceDbo createUserCompanyReferenceDbo(@NotNull UserDbo user, @NotNull CompanyDbo company) {
        return UserCompanyReferenceDbo.builder()
                .userReference(user)
                .companyReference(company)
                .build();

    }
}
