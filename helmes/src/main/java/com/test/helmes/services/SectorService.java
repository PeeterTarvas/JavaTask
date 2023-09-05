package com.test.helmes.services;


import com.test.helmes.dbos.SectorDbo;
import com.test.helmes.dtos.SectorDto;
import com.test.helmes.repositories.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectorService {


    @Autowired
    private SectorRepository sectorRepository;


    public List<SectorDto> getSectorDtos() {
        return sectorRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private SectorDto convertToDto(SectorDbo sectorDbo) {
        return SectorDto.builder()
                .sectorName(sectorDbo.getSectorName())
                .sectorId(sectorDbo.getSectorId())
                .sectorParentId(sectorDbo.getSectorParentId())
                .build();
    }
}
