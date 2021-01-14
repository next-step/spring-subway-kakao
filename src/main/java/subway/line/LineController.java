package subway.line;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class LineController {

    private static LineDao lineDao = new LineDao();

    @PostMapping(value = "/lines", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {

        Line line = new Line( lineRequest.getName(), lineRequest.getColor());

        if( lineDao.hasContains(line) ) {
            return ResponseEntity.badRequest().build();
        }

        lineDao.save(line);

        LineResponse lineResponse = new LineResponse(line.getId(), line.getName(), line.getColor(), null);

        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(lineResponse);
    }

//    @GetMapping

}
