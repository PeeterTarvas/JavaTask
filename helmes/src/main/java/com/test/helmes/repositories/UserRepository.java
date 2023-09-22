package com.test.helmes.repositories;


import com.test.helmes.dbos.UserDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserDbo, Integer> {

    Optional<UserDbo> getUserDboByUsername(String username);
}
