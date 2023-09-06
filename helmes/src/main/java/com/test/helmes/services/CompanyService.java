package com.test.helmes.services;


import com.test.helmes.dbos.CompanyDbo;
import com.test.helmes.dtos.CompanyDto;
import com.test.helmes.errors.NoDataExistsException;
import com.test.helmes.errors.InvalidDataException;
import com.test.helmes.repositories.CompanyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Transactional(rollbackOn = InvalidDataException.class)
    public void saveCompany(CompanyDto companyDto) throws InvalidDataException {
            if (isValid(companyDto)) {
                try {
                    companyRepository.saveCompany(convertToCompanyDbo(companyDto));
                } catch (NoDataExistsException e) {
                    throw new InvalidDataException(e.getMessage());
                }
            } else {
                throw new InvalidDataException("Missing input");
            }
    }

    public List<CompanyDto> getAll() {
        return companyRepository.findAll().stream().map(this::convertToCompanyDto).toList();
    }

    private boolean isValid(CompanyDto companyDto) {
        return companyDto.getCompanyTerms()
                && isValidName(companyDto.getCompanyName())
                && companyDto.getCompanySectorId() != null;
    }

    private boolean isValidName(String name) {
        return !name.trim().isEmpty();
    }

    private CompanyDbo convertToCompanyDbo(CompanyDto companyDto) {
        return CompanyDbo.builder()
                .companyName(companyDto.getCompanyName())
                .companySectorId(companyDto.getCompanySectorId())
                .companyTerms(companyDto.getCompanyTerms())
                .build();
    }

    private CompanyDto convertToCompanyDto(CompanyDbo companyDbo) {
        return CompanyDto.builder()
                .companyName(companyDbo.getCompanyName())
                .companySectorId(companyDbo.getCompanySectorId())
                .companyTerms(companyDbo.getCompanyTerms())
                .build();
    }
}
