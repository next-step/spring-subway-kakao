package subway.station;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.exceptions.DuplicateLineNameException;
import subway.exceptions.DuplicateStationNameException;

import javax.swing.text.html.parser.Entity;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class StationController {

    @ExceptionHandler(DuplicateStationNameException.class)
    public ResponseEntity<String> errorHandler(DuplicateStationNameException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @PostMapping("/stations")
    public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
        Station station = new Station(stationRequest.getName());
        Station newStation;
        StationResponse stationResponse;
        newStation = StationDao.save(station);
        stationResponse = new StationResponse(newStation.getId(), newStation.getName());
        return ResponseEntity.created(URI.create("/stations/" + newStation.getId())).body(stationResponse);
    }

    @GetMapping(value = "/stations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StationResponse>> showStations() {
        List<StationResponse> responses = new ArrayList<>();
        for (Station station : StationDao.findAll()) {
            responses.add(new StationResponse(station.getId(), station.getName()));
        }
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity deleteStation(@PathVariable Long id) {
        if (StationDao.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
