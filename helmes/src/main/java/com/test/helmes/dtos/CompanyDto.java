package com.test.helmes.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class CompanyDto {

    private String companyName;

    private Integer companySectorId;

    private Boolean companyTerms;
}
