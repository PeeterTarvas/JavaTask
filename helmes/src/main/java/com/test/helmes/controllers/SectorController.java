package com.test.helmes.controllers;

import com.test.helmes.dtos.SectorDto;
import com.test.helmes.services.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@RequestMapping("/sector")
public class SectorController {

    @Autowired
    SectorService sectorService;

    /**
     * @return all the sectors in the database.
     */
    @GetMapping("/getAll")
    public List<SectorDto> getSectors() {
            return sectorService.getSectorDtos();
        }
}
