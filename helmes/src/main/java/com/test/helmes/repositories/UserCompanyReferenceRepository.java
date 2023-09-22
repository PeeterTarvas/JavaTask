package com.test.helmes.repositories;

import com.test.helmes.dbos.UserCompanyReferenceDbo;
import com.test.helmes.dbos.UserDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCompanyReferenceRepository extends JpaRepository<UserCompanyReferenceDbo, Integer> {

    Optional<UserCompanyReferenceDbo> getUserCompanyReferenceDboByUserReference(UserDbo userDbo);

}
