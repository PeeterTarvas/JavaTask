package com.test.helmes.services;


import com.test.helmes.dbos.CompanyDbo;
import com.test.helmes.dbos.UserCompanyReferenceDbo;
import com.test.helmes.dbos.UserDbo;
import com.test.helmes.dtos.CompanyDto;
import com.test.helmes.repositories.CompanyRepository;
import com.test.helmes.repositories.UserCompanyReferenceRepository;
import com.test.helmes.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.test.helmes.errors.Error;

import java.util.Optional;

/**
 * Service for handling all company related logic.
 */
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final MapperService mapperService;
    private final UserRepository userRepository;
    private final UserCompanyReferenceRepository userCompanyReferenceRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository,
                          MapperService mapperService,
                          UserRepository userRepository,
                          UserCompanyReferenceRepository userCompanyReferenceRepository
                          ) {
        this.companyRepository = companyRepository;
        this.mapperService = mapperService;
        this.userRepository = userRepository;
        this.userCompanyReferenceRepository = userCompanyReferenceRepository;
    }


    /**
     * This method is for saving the users company.
     * First it checks if the user is a valid user(exists) and then if the company details are correct.
     *  - if true then create a new company for the user with saveNewCompany and return false
     * @param username of the user who wants to save the company.
     * @param companyDto that holds the company details.
     * @throws Error if user has invalid details about company or user is not present.
     */
    @Transactional()
    public void saveCompany(String username, CompanyDto companyDto) throws Error {
        Optional<UserDbo> userDbo = userRepository.getUserDboByUsername(username);
        if (userDbo.isPresent()) {
            saveNewCompany(userDbo.get(), companyDto);
        } else {
            throw new Error("Missing input!");
        }
    }

    /**
     * This method is for updating the users company.
     * Firstly it checks if the user exists
     *  - if true then calls updateCompanyDetails for updating the company.
     * @param username of the user.
     * @param companyDto that holds the updated details.
     * @throws Error if the details of the company are wrong or user is not present.
     */
    @Transactional()
    public void updateCompany(String username, CompanyDto companyDto) throws Error {
        Optional<UserDbo> userDbo = userRepository.getUserDboByUsername(username);
        if (userDbo.isPresent()) {
            Optional<UserCompanyReferenceDbo> userCompanyReference = userHasCompany(userDbo.get());
            userCompanyReference.ifPresent(userCompanyReferenceDbo ->
                    updateCompanyDetails(companyDto, userCompanyReferenceDbo));
        } else {
            throw new Error("Missing input!");
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
    public Optional<CompanyDto> getUsersCompany(String username) throws Error {
        Optional<UserDbo> userDbo = userRepository.getUserDboByUsername(username);
        if (userDbo.isPresent()) {
            Optional<UserCompanyReferenceDbo> userCompanyReferenceDbo = userHasCompany(userDbo.get());
            if (userCompanyReferenceDbo.isPresent()) {
                return Optional.of(
                        mapperService.convertToCompanyDto(userCompanyReferenceDbo.get()
                                .getCompanyReference()
                        )
                );
            }
        }
        throw new Error("Bad input!");
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
        companyRepository.saveCompany(mapperService.convertToCompanyDbo(companyDto));
        Optional<CompanyDbo> company = companyRepository.getCompanyDboByCompanyName(companyDto.getCompanyName());
        UserCompanyReferenceDbo userCompanyReferenceDbo = mapperService.createUserCompanyReferenceDbo(userDbo, company.get());
        userCompanyReferenceRepository.save(userCompanyReferenceDbo);
    }


}
