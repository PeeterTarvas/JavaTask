package com.test.helmes.controllers.sector;

import com.test.helmes.controllers.helper.ResponseHandler;
import com.test.helmes.services.sector.SectorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This is the endpoint controller for the sectors in the back-end.
 */
@Slf4j
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
        log.info("Getting sectors");
        return responseHandler.returnResponse(HttpStatus.OK, sectorService.getSectorDtos());
        }
}
