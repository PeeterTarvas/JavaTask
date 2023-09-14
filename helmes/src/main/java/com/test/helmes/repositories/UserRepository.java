package com.test.helmes.repositories;


import com.test.helmes.dbos.UserDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserDbo, Integer> {

    UserDbo getUserDboByUsername(String username);
}
