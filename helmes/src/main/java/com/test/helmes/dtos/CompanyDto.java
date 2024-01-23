package com.test.helmes.dtos;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * This object is for sending details of the company between the front-end and back-end.
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
public class CompanyDto {

    @NotBlank
    private String companyName;

    @NotNull
    private Integer companySectorId;

    @NotNull
    @AssertTrue
    private Boolean companyTerms;
}
