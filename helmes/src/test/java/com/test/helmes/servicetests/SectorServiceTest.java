package com.test.helmes.servicetests;

import com.test.helmes.dbos.SectorDbo;
import com.test.helmes.dtos.SectorDto;
import com.test.helmes.repositories.SectorRepository;
import com.test.helmes.services.ConverterService;
import com.test.helmes.services.SectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class SectorServiceTest {

    @MockBean
    private SectorRepository sectorRepository;

    @MockBean
    private ConverterService converterService;

    @Autowired
    private SectorService sectorService;


    @BeforeEach
    public void setUp() {
        // Reset mock interactions before each test
        Mockito.reset(sectorRepository);
        Mockito.reset(converterService);
    }

    @Test
    public void testGetSectorDtos() {
        // Arrange
        List<SectorDbo> sectorDbos = Arrays.asList(
                new SectorDbo(1,"Sector1", 0),
                new SectorDbo(2,"Sector2", 0)
        );

        List<SectorDto> expectedSectorDtos = Arrays.asList(
                new SectorDto(1,"Sector1", 0),
                new SectorDto(2,"Sector2", 0)
        );

        when(sectorRepository.findAll()).thenReturn(sectorDbos);
        when(converterService.convertToSectorDto(sectorDbos.get(0))).thenReturn(expectedSectorDtos.get(0));
        when(converterService.convertToSectorDto(sectorDbos.get(1))).thenReturn(expectedSectorDtos.get(1));

        // Act
        List<SectorDto> resultSectorDtos = sectorService.getSectorDtos();

        // Assert
        assertEquals(expectedSectorDtos.size(), resultSectorDtos.size());
        for (int i = 0; i < expectedSectorDtos.size(); i++) {
            assertEquals(expectedSectorDtos.get(i), resultSectorDtos.get(i));
        }
    }

}
