package com.test.helmes.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class SectorDto {

    private Integer sectorId;

    private String sectorName;

    private Integer sectorParentId;


}
