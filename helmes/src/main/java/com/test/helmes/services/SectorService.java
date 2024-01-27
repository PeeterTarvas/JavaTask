package com.test.helmes.services;


import com.test.helmes.dtos.SectorDto;
import com.test.helmes.repositories.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for sector related method.
 */
@Service
public class SectorService {


    private final SectorRepository sectorRepository;

    private final MapperService mapperService;

    @Autowired
    public SectorService(SectorRepository sectorRepository, MapperService mapperService) {
        this.sectorRepository = sectorRepository;
        this.mapperService = mapperService;
    }


    /**
     * @return all the sectors in the database,
     * is used for fetching the sector-select components options in the front-end part
     */
    public List<SectorDto> getSectorDtos() {
        return sectorRepository.findAll()
                .stream()
                .map(mapperService::convertToSectorDto)
                .collect(Collectors.toList());
    }

}
