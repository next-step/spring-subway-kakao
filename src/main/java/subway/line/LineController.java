package subway.line;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import subway.station.Station;
import subway.station.StationDao;
import subway.station.StationResponse;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LineController {

    LineDao lineDao = new LineDao();
    StationDao stationDao = new StationDao();

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest){

        if(lineDao.isContainSameName(lineRequest.getName())){
            return ResponseEntity.badRequest().build();
        }
        Line newLine= lineDao.save(new Line(lineRequest));
        newLine.add(stationDao.findById(lineRequest.getUpStationId()));
        newLine.add(stationDao.findById(lineRequest.getDownStationId()));
        return ResponseEntity.created(
                URI.create("/line/" +newLine.getId()))
                .body(new LineResponse(newLine.getId(),newLine.getName(),newLine.getColor(),newLine.getStations().stream().map(StationResponse::new).collect(Collectors.toList())));
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> getLines(){
        List<LineResponse> response = lineDao.findAll()
                .stream()
                .map(LineResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/lines/{lineId}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long lineId){
        Line searchedLine=lineDao.findById(lineId);
        return ResponseEntity.ok().body(new LineResponse(searchedLine.getId(),searchedLine.getName(),searchedLine.getColor(),searchedLine.getStations().stream().map(StationResponse::new).collect(Collectors.toList())));
    }

    @PutMapping("/lines/{lineId}")
    public ResponseEntity<LineResponse> updateLine(@PathVariable Long lineId, @RequestBody LineRequest lineRequest){
        lineDao.modify(lineId, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{lineId}")
    public ResponseEntity<LineResponse> deleteLine(@PathVariable Long lineId){
        lineDao.delete(lineId);
        return  ResponseEntity.noContent().build();
    }


}
