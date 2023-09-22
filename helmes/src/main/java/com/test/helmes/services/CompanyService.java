package com.test.helmes.services;


import com.test.helmes.dbos.CompanyDbo;
import com.test.helmes.dbos.UserCompanyReferenceDbo;
import com.test.helmes.dbos.UserDbo;
import com.test.helmes.dtos.CompanyDto;
import com.test.helmes.errors.NoDataExistsException;
import com.test.helmes.errors.InvalidDataException;
import com.test.helmes.repositories.CompanyRepository;
import com.test.helmes.repositories.UserCompanyReferenceRepository;
import com.test.helmes.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private CompanyRepository companyRepository;
    private ConverterService converterService;
    private UserRepository userRepository;
    private UserCompanyReferenceRepository userCompanyReferenceRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository,
                          ConverterService converterService,
                          UserRepository userRepository,
                          UserCompanyReferenceRepository userCompanyReferenceRepository
                          ) {
        this.companyRepository = companyRepository;
        this.converterService = converterService;
        this.userRepository = userRepository;
        this.userCompanyReferenceRepository = userCompanyReferenceRepository;
    }
    /**
     * saveCompany method is a service method that saves the company, if valid, with the help of repository.
     * If companyDto's attributes are all valid then it will be saved through the companyRepository method.
     * If the company already exists then it will throw an error and will not save the company
     *  -- future should be altering the company, not sure if user validation should be here as well
     * @param companyDto of the company that is being saved
     * @throws InvalidDataException if data is invalid or already exists
     */
    @Transactional(rollbackOn = InvalidDataException.class)
    public boolean saveCompany(String username, CompanyDto companyDto) throws InvalidDataException {
        Optional<UserDbo> userDbo = userRepository.getUserDboByUsername(username);
        if (isValid(companyDto) && userDbo.isPresent()) {
            try {
                Optional<UserCompanyReferenceDbo> userCompanyReference = userHasCompany(userDbo.get());
                if (userCompanyReference.isPresent()) {
                    updateCompanyDetails(companyDto, userCompanyReference.get());
                    return true;
                } else {
                    saveNewCompany(userDbo.get(), companyDto);
                    return false;
                }
            } catch (NoDataExistsException e) {
                throw new InvalidDataException("Bad input!");
            }
        } else {
            throw new InvalidDataException("Missing input!");
        }
    }

    private void updateCompanyDetails(CompanyDto companyDto, UserCompanyReferenceDbo userCompanyReferenceDbo) {
        CompanyDbo company = userCompanyReferenceDbo.getCompanyReference();
        company.setCompanyName(companyDto.getCompanyName());
        company.setCompanySectorId(companyDto.getCompanySectorId());
        company.setCompanyTerms(companyDto.getCompanyTerms());
        companyRepository.save(company);
    }

    private Optional<UserCompanyReferenceDbo> userHasCompany(UserDbo userDbo) {
        return userCompanyReferenceRepository.getUserCompanyReferenceDboByUserReference(userDbo);
    }

    private void saveNewCompany(UserDbo userDbo, CompanyDto companyDto) {
        companyRepository.saveCompany(converterService.convertToCompanyDbo(companyDto));
        Optional<CompanyDbo> company = companyRepository.getCompanyDboByCompanyName(companyDto.getCompanyName());
        userCompanyReferenceRepository.save(converterService.createUserCompanyReferenceDbo(userDbo, company.get()));
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
