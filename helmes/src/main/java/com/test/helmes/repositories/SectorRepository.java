package com.test.helmes.repositories;


import com.test.helmes.dbos.SectorDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectorRepository extends JpaRepository<SectorDbo, Integer> {

}
