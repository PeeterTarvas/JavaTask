package com.test.helmes.repositories;

import com.test.helmes.dbos.UserCompanyReferenceDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCompanyReferenceRepository extends JpaRepository<UserCompanyReferenceDbo, Integer> {
}
