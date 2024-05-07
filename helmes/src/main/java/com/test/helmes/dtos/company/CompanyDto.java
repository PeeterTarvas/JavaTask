package com.test.helmes.dtos.company;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * This object is for sending details of the company between the front-end and back-end.
 */
@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
public class CompanyDto {

    @NotBlank
    private String companyName;

    @NotNull
    @Min(value = 1, message = "Must be a positive number")
    @Max(value = 80, message = "Must not be over 80")
    private Integer companySectorId;

    @NotNull
    @AssertTrue
    private Boolean companyTerms;
}
