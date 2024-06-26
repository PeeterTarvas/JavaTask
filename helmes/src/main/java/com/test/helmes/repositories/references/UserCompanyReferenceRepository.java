package com.test.helmes.repositories.references;

import com.test.helmes.dbos.references.UserCompanyReferenceDbo;
import com.test.helmes.dbos.user.UserDbo;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository that references helmes.user_company_reference table in the database.
 */
@Repository
public interface UserCompanyReferenceRepository extends JpaRepository<UserCompanyReferenceDbo, Integer> {

    /**
     * This method makes the reference object accessible by the userDbo.
     * @param userDbo is holds the users details who's company we want to get with the reference object.
     * @return optional of the reference object.
     */
    Optional<UserCompanyReferenceDbo> getUserCompanyReferenceDboByUserReference(@Valid UserDbo userDbo);

}
