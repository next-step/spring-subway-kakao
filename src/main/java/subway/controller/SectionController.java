package subway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import subway.domain.section.Section;
import subway.domain.section.SectionRequest;
import subway.domain.section.SectionResponse;
import subway.service.SectionService;

import java.net.URI;

@Controller
public class SectionController {
    private final SectionService sectionService;

    @Autowired
    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<SectionResponse> createSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        Section newSection = sectionService.createSection(sectionRequest.toSection(lineId));
        return ResponseEntity.created(URI.create("/lines/" + lineId + "/sections/" + newSection.getId())).body(new SectionResponse(newSection));
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        sectionService.deleteSection(lineId, stationId);
        return ResponseEntity.ok().build();
    }
}
