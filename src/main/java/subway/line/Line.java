package subway.line;

import subway.exception.NoContentException;
import subway.exception.TwoStationException;
import subway.station.Station;

import java.util.*;
import java.util.stream.IntStream;

public class Line {
    private Long id;
    private String name;
    private String color;
    private final SectionGroup sections;

    public Line() {
        this.sections = new SectionGroup();
    }

    public Line(String name, String color) {
        this();
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color);
        sections.insertFirstSection(upStation, downStation, distance);
    }

    public Section addSection(Station upStation, Station downStation, int distance) {
        if (sections.isEmpty()) {
            return sections.insertFirstSection(upStation, downStation, distance);
        }

        return sections.makeAndInsertSection(upStation, downStation, distance);
    }

    public void deleteStation(Long stationId) {
        sections.deleteStation(stationId);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", sections=" + sections +
                '}';
    }

}
