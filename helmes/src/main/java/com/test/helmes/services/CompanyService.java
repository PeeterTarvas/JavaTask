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
     * This method is for updating or saving the users company.
     * First it checks if the user is a valid user(exists) and then if the company details are correct.
     *  - if true then find if the user already has a company,
     *      - if yes then update that company with updateCompanyDetails and return true.
     *      - else create a new company for the user with saveNewCompany and return false
     * @param username of the user who wants to save the company.
     * @param companyDto that holds the company details.
     * @return true if the company has been updated and false if it is saved the first time.
     * @throws InvalidDataException if user has invalid details about company or somehow doesn't have a account.
     */
    @Transactional(rollbackOn = InvalidDataException.class)
    public boolean updateOrSaveCompany(String username, CompanyDto companyDto) throws InvalidDataException {
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

    /**
     * Method for updating company in the database. This is called in saveCompany method
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
        userCompanyReferenceRepository.save(converterService.createUserCompanyReferenceDbo(userDbo, company.get()));
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
    public Optional<CompanyDto> getUsersCompany(String username) {
        Optional<UserDbo> userDbo = userRepository.getUserDboByUsername(username);
        if (userDbo.isPresent()) {
            Optional<UserCompanyReferenceDbo> userCompanyReferenceDbo = userHasCompany(userDbo.get());
            if (userCompanyReferenceDbo.isPresent()) {
                return Optional.of(
                        converterService.convertToCompanyDto(userCompanyReferenceDbo.get().getCompanyReference()));
            }
        }
        return Optional.empty();
    }

    /**
     * @return all the companies, this is for checking the database from back-end
     */
    public List<CompanyDto> getAll() {
        return companyRepository.findAll().stream().map(converterService::convertToCompanyDto).toList();
    }

    /**
     * Controls if company has accepted terms, has a valid name and that the sectorId isn't null, i.e. exists
     * @return a boolean if companyDto attributes are valid or not
     */
    private boolean isValid(CompanyDto companyDto) {
        return companyDto.getCompanyTerms()
                && isValidName(companyDto.getCompanyName())
                && companyDto.getCompanySectorId() != null;
    }

    /**
     * @return if the name isn't an empty string.
     */
    private boolean isValidName(String name) {
        return !name.trim().isEmpty();
    }

}
