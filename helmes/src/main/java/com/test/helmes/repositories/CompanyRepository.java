package com.test.helmes.repositories;

import com.test.helmes.dbos.CompanyDbo;
import com.test.helmes.errors.NoDataExistsException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyDbo, Integer> {

    /**
     * This method tries to save the company in the database and
     * if it doesn't work then throws an exception that the data already exists
     */
    default <S extends CompanyDbo> void saveCompany(S companyDbo) {
        try {
            saveAndFlush(companyDbo);
        } catch (DataIntegrityViolationException ex) {
            throw new NoDataExistsException("Company name already exists: " + companyDbo.getCompanyName());
        }
    }

    Optional<CompanyDbo> getCompanyDboByCompanyName(String companyName);

}
