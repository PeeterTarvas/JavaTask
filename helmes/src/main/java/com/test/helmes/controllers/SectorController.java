package com.test.helmes.controllers;

import com.test.helmes.services.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This is the endpoint controller for the sectors in the back-end.
 */
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@RequestMapping("/sector")
public class SectorController {

    SectorService sectorService;

    @Autowired
    public SectorController(SectorService sectorService) {
        this.sectorService = sectorService;
    }

    /**
     * @return all the sectors in the database. This endpoint is called when the user logs-in.
     */
    @GetMapping("/getAll")
    public ResponseEntity<?> getSectors() {
            return ResponseEntity.status(HttpStatus.OK).body(sectorService.getSectorDtos());
        }
}
