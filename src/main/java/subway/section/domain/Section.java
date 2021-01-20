package subway.section.domain;

public class Section {
    private final Long id;
    private final long lineId;
    private final long stationId;
    private final int position;

    public Section(Long id, long lineId, long stationId, int position) {
        this.id = id;
        this.lineId = lineId;
        this.stationId = stationId;
        this.position = position;
    }

    public Section(long lineId, long stationId, int position) {
        this(null, lineId, stationId, position);
    }

    public boolean hasStation(long stationId) {
        return this.stationId == stationId;
    }

    public int getDifferenceOfPosition(Section section) {
        return Math.abs(position - section.position);
    }

    public int calculateNextDownPosition(int distance) {
        return position + distance;
    }

    public int calculateNextUpPosition(int distance) {
        return position - distance;
    }

    public Long getId() {
        return id;
    }

    public long getLineId() {
        return lineId;
    }

    public long getStationId() {
        return stationId;
    }

    public int getPosition() {
        return position;
    }
}
