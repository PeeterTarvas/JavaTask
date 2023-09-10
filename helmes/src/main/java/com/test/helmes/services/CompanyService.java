package com.test.helmes.services;


import com.test.helmes.dtos.CompanyDto;
import com.test.helmes.errors.NoDataExistsException;
import com.test.helmes.errors.InvalidDataException;
import com.test.helmes.repositories.CompanyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ConverterService converterService;

    /**
     * saveCompany method is a service method that saves the company, if valid, with the help of repository.
     * If companyDto's attributes are all valid then it will be saved through the companyRepository method.
     * If the company already exists then it will throw an error and will not save the company
     *  -- future should be altering the company, not sure if user validation should be here as well
     * @param companyDto of the company that is being saved
     * @throws InvalidDataException if data is invalid or already exists
     */
    @Transactional(rollbackOn = InvalidDataException.class)
    public void saveCompany(CompanyDto companyDto) throws InvalidDataException {
            if (isValid(companyDto)) {
                try {
                    companyRepository.saveCompany(converterService.convertToCompanyDbo(companyDto));
                } catch (NoDataExistsException e) {
                    throw new InvalidDataException(e.getMessage());
                }
            } else {
                throw new InvalidDataException("Missing input");
            }
    }

    /**
     * @return all the companies, this is for checking the database from back-end
     */
    public List<CompanyDto> getAll() {
        return companyRepository.findAll().stream().map(x -> converterService.convertToCompanyDto(x)).toList();
    }

    /**
     * Controls if company has accepted terms, has a valid name and that the sectorId isn't null, i.e. exists
     * @return a boolean if companyDtos attributes are valid or not
     */
    private boolean isValid(CompanyDto companyDto) {
        return companyDto.getCompanyTerms()
                && isValidName(companyDto.getCompanyName())
                && companyDto.getCompanySectorId() != null;
    }

    /**
     * @return if the name isn't an empty string, mby should add UTF-8 check here as well?
     */
    private boolean isValidName(String name) {
        return !name.trim().isEmpty();
    }

}
