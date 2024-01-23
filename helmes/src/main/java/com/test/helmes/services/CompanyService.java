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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for handling all company related logic.
 */
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final ConverterService converterService;
    private final UserRepository userRepository;
    private final UserCompanyReferenceRepository userCompanyReferenceRepository;

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
     * This method is for saving the users company.
     * First it checks if the user is a valid user(exists) and then if the company details are correct.
     *  - if true then create a new company for the user with saveNewCompany and return false
     * @param username of the user who wants to save the company.
     * @param companyDto that holds the company details.
     * @throws InvalidDataException if user has invalid details about company or user is not present.
     */
    @Transactional()
    public void saveCompany(String username, CompanyDto companyDto) throws InvalidDataException {
        Optional<UserDbo> userDbo = userRepository.getUserDboByUsername(username);
        if (userDbo.isPresent()) {
            try {
                saveNewCompany(userDbo.get(), companyDto);
            } catch (NoDataExistsException e) {
                throw new InvalidDataException("Bad input!");
            }
        } else {
            throw new InvalidDataException("Missing input!");
        }
    }

    /**
     * This method is for updating the users company.
     * Firstly it checks if the user exists
     *  - if true then calls updateCompanyDetails for updating the company.
     * @param username of the user.
     * @param companyDto that holds the updated details.
     * @throws InvalidDataException if the details of the company are wrong or user is not present.
     */
    @Transactional()
    public void updateCompany(String username, CompanyDto companyDto) throws InvalidDataException {
        Optional<UserDbo> userDbo = userRepository.getUserDboByUsername(username);
        if (userDbo.isPresent()) {
            try {
                Optional<UserCompanyReferenceDbo> userCompanyReference = userHasCompany(userDbo.get());
                userCompanyReference.ifPresent(userCompanyReferenceDbo ->
                        updateCompanyDetails(companyDto, userCompanyReferenceDbo));
            } catch (NoDataExistsException e) {
                throw new InvalidDataException("Bad input!");
            }
        } else {
            throw new InvalidDataException("Missing input!");
        }
    }

    /**
     * This method is for getting the company of a user by only providing a username of the user.
     * This is mainly used when user logs-in and ngInit requests the users company to display already
     * made company's info.
     * First check if the user exists by getting the users database object by his name.
     *  - if the user exists then check for a user-company reference object in the database
     *   - if the reference exists then get the references company object and return it as an optional
     *  else return an empty optional.
     * @param username of the account who requests his company.
     * @return an optional of the company data transfer object(CompanyDto).
     */
    @Transactional()
    public Optional<CompanyDto> getUsersCompany(String username) {
        Optional<UserDbo> userDbo = userRepository.getUserDboByUsername(username);
        if (userDbo.isPresent()) {
            Optional<UserCompanyReferenceDbo> userCompanyReferenceDbo = userHasCompany(userDbo.get());
            if (userCompanyReferenceDbo.isPresent()) {
                return Optional.of(
                        converterService.convertToCompanyDto(userCompanyReferenceDbo.get()
                                .getCompanyReference()
                        )
                );
            }
        }
        return Optional.empty();
    }


    /**
     * Method for updating company in the database. This is called in updateCompany method.
     * if the user already has a company in the database. Then that company is updated with the new details.
     */
    private void updateCompanyDetails(CompanyDto companyDto, UserCompanyReferenceDbo userCompanyReferenceDbo) {
        CompanyDbo company = userCompanyReferenceDbo.getCompanyReference();
        company.setCompanyName(companyDto.getCompanyName());
        company.setCompanySectorId(companyDto.getCompanySectorId());
        company.setCompanyTerms(companyDto.getCompanyTerms());
        companyRepository.save(company);
    }

    /**
     * Checks if the user has a company already accosted with his account.
     * If the user has a company then its reference object is wrapped in an optional and returned.
     * @param userDbo that holds the details of the user whos company we are searching for.
     * @return an Optional of the user and company reference object.
     */
    private Optional<UserCompanyReferenceDbo> userHasCompany(UserDbo userDbo) {
        return userCompanyReferenceRepository.getUserCompanyReferenceDboByUserReference(userDbo);
    }

    /**
     * Saves a new company to the company database and also saves a reference
     * referring to the user and company.
     * @param userDbo that holds the user database object.
     * @param companyDto that holds the company database object.
     */
    private void saveNewCompany(UserDbo userDbo, CompanyDto companyDto) {
        companyRepository.saveCompany(converterService.convertToCompanyDbo(companyDto));
        Optional<CompanyDbo> company = companyRepository.getCompanyDboByCompanyName(companyDto.getCompanyName());
        UserCompanyReferenceDbo userCompanyReferenceDbo = converterService.createUserCompanyReferenceDbo(userDbo, company.get());
        userCompanyReferenceRepository.save(userCompanyReferenceDbo);
    }

    /**
     * Controls if company has accepted terms, has a valid name and that the sectorId isn't null, i.e. exists
     * @return a boolean if companyDto attributes are valid or not
     */
    public boolean isValid(CompanyDto companyDto) {
        return companyDto.getCompanyTerms()
                && isValidName(companyDto.getCompanyName())
                && companyDto.getCompanySectorId() != null;
    }

    /**
     * @return if the name isn't an empty string.
     */
    public boolean isValidName(String name) {
        if (name != null) {
            return !name.trim().isEmpty();
        }
        return false;
    }

}
