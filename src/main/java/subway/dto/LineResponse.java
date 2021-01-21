package subway.dto;

import subway.domain.Line;

import java.util.List;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private List<StationResponse> stations;

    public LineResponse () {}

    public LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse of(Line line, List<StationResponse> stations) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
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

    public List<StationResponse> getStations() {
        return stations;
    }
}
