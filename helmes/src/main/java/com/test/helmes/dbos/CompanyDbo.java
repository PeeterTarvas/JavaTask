package com.test.helmes.dbos;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

/**
 * This class represents a company in the database.
 */
@Builder
@Setter
@Getter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company", schema = "helmes")
public class CompanyDbo {

    @Id
    @Column(name = "company_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_sector_id")
    private Integer companySectorId;

    @Column(name = "company_terms")
    private Boolean companyTerms;
}
