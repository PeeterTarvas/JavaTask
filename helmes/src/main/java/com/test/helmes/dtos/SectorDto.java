package com.test.helmes.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * This object is for sending the sectors details between the front-end and back-end.
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
public class SectorDto {

    private Integer sectorId;

    private String sectorName;

    private Integer sectorParentId;


}
