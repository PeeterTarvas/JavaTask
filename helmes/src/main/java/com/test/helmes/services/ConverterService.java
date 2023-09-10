package com.test.helmes.services;

import com.test.helmes.dbos.CompanyDbo;
import com.test.helmes.dbos.SectorDbo;
import com.test.helmes.dtos.CompanyDto;
import com.test.helmes.dtos.SectorDto;
import org.springframework.stereotype.Service;

/**
 * This class is for conversion methods, i.e. convert from dto to dbo or vice versa.
 */

@Service
public class ConverterService {

    public CompanyDbo convertToCompanyDbo(CompanyDto companyDto) {
        return CompanyDbo.builder()
                .companyName(companyDto.getCompanyName())
                .companySectorId(companyDto.getCompanySectorId())
                .companyTerms(companyDto.getCompanyTerms())
                .build();
    }

    public CompanyDto convertToCompanyDto(CompanyDbo companyDbo) {
        return CompanyDto.builder()
                .companyName(companyDbo.getCompanyName())
                .companySectorId(companyDbo.getCompanySectorId())
                .companyTerms(companyDbo.getCompanyTerms())
                .build();
    }

    public SectorDto convertToSectorDto(SectorDbo sectorDbo) {
        return SectorDto.builder()
                .sectorName(sectorDbo.getSectorName())
                .sectorId(sectorDbo.getSectorId())
                .sectorParentId(sectorDbo.getSectorParentId())
                .build();
    }
}