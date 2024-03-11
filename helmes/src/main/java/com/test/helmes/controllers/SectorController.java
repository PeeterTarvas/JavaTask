package com.test.helmes.controllers;

import com.test.helmes.controllers.helper.ResponseHandler;
import com.test.helmes.services.SectorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This is the endpoint controller for the sectors in the back-end.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sector")
public class SectorController {

    private final SectorService sectorService;

    private final ResponseHandler responseHandler;


    /**
     * @return all the sectors in the database. This endpoint is called when the user logs-in.
     */
    @GetMapping("/getAll")
    public ResponseEntity<?> getSectors() {
            return responseHandler.returnResponse(HttpStatus.OK, sectorService.getSectorDtos());
        }
}
