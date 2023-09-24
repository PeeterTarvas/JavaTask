package com.test.helmes.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * This object is for sending details of the company between the front-end and back-end.
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
public class CompanyDto {

    private String companyName;

    private Integer companySectorId;

    private Boolean companyTerms;
}
