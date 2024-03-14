package com.test.helmes.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * This object is for sending the sectors details between the front-end and back-end.
 */
@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
public class SectorDto {

    @NotNull
    private Integer sectorId;

    @NotBlank
    private String sectorName;

    @NotNull
    private Integer sectorParentId;


}
