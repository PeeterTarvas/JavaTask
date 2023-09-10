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

    @Autowired
    private ConverterService converterService;

    /**
     * @return all the sectors in the database,
     * is used for fetching the sector-select components options in the front-end part
     */
    public List<SectorDto> getSectorDtos() {
        return sectorRepository.findAll()
                .stream()
                .map(x -> converterService.convertToSectorDto(x))
                .collect(Collectors.toList());
    }

}
