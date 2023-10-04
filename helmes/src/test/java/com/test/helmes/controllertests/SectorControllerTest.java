package com.test.helmes.controllertests;


import com.test.helmes.controllers.SectorController;
import com.test.helmes.dtos.SectorDto;
import com.test.helmes.services.SectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SectorControllerTest {


    @MockBean
    private SectorService sectorService;

    @Autowired
    private SectorController sectorController;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test getting all the sectors.
     */
    @Test
    public void testGetSectors() {
        List<SectorDto> sectorDtoList = Arrays.asList(
                new SectorDto(1, "Sector A", null),
                new SectorDto(2, "Sector B", null)
        );
        when(sectorService.getSectorDtos()).thenReturn(sectorDtoList);

        ResponseEntity<List<SectorDto>> resp = (ResponseEntity<List<SectorDto>>) sectorController.getSectors();

        verify(sectorService, times(1)).getSectorDtos();
        assertEquals(resp.getBody(), sectorDtoList);
        assertEquals(Objects.requireNonNull(resp.getBody()).get(0).getSectorId(), sectorDtoList.get(0).getSectorId());
        assertEquals(Objects.requireNonNull(resp.getBody()).get(1).getSectorId(), sectorDtoList.get(1).getSectorId());

    }


}
