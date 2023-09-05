package com.test.helmes.repositories;

import com.test.helmes.dbos.CompanyDbo;
import com.test.helmes.errors.NoDataExistsException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyDbo, Integer> {

    default <S extends CompanyDbo> S saveCompany(S companyDbo) {
        try {
            return saveAndFlush(companyDbo);
        } catch (DataIntegrityViolationException ex) {
            throw new NoDataExistsException("Company name already exists: " + companyDbo.getCompanyName());
        }
    }
}
