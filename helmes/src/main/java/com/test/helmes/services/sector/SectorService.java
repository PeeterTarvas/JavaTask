package com.test.helmes.services.sector;



import com.test.helmes.dtos.sector.SectorDto;
import com.test.helmes.repositories.sector.SectorRepository;
import com.test.helmes.services.mappers.MapperService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for sector related method.
 */
@AllArgsConstructor
@Service
public class SectorService {


    private final SectorRepository sectorRepository;

    private final MapperService mapperService;


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
