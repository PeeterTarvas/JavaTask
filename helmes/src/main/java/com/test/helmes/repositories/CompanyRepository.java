package com.test.helmes.repositories;

import com.test.helmes.dbos.CompanyDbo;
import com.test.helmes.errors.NoDataExistsException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository that references helmes.user_company_reference table in the database.
 */
@Repository
public interface CompanyRepository extends JpaRepository<CompanyDbo, Integer> {

    /**
     * This method tries to save or update the company in the database and
     * if the save or update doesn't work then throws an exception that the data already exists
     */
    default <S extends CompanyDbo> void saveCompany(S companyDbo) {
        try {
            saveAndFlush(companyDbo);
        } catch (DataIntegrityViolationException ex) {
            throw new NoDataExistsException("Company name already exists: " + companyDbo.getCompanyName());
        }
    }

    /**
     * This method is for accessing a company object by its username.
     * @param companyName of the company that is requested.
     * @return an optional of the company object
     */
    Optional<CompanyDbo> getCompanyDboByCompanyName(String companyName);

}
