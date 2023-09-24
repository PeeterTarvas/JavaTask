package com.test.helmes.repositories;


import com.test.helmes.dbos.SectorDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository that references helmes.sector table in the database.
 */
@Repository
public interface SectorRepository extends JpaRepository<SectorDbo, Integer> {

}
