package subway.line;

import java.util.Objects;

public class Line {

    private Long id;
    private final String name;
    private final String color;
    private final Long startStationId;
    private final Long endStationId;

    public Line(String name, String color, Long startStationId, Long endStationId) {
        this.name = name;
        this.color = color;
        this.startStationId = startStationId;
        this.endStationId = endStationId;
    }

    public Line(Long id, String name, String color, Long startStationId, Long endStationId) {
        this(name, color, startStationId, endStationId);
        this.id = id;
    }

    public boolean isLineStartStation(Long stationId) {
        return startStationId == stationId;
    }

    public boolean isLineEndStation(Long stationId) {
        return endStationId == stationId;
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

    public Long getStartStationId() { return startStationId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(name, line.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
