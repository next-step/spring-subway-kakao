package subway.line;

import org.springframework.stereotype.Service;
import subway.exception.exceptions.DuplicateLineNameException;
import subway.exception.exceptions.FailedDeleteLineException;
import subway.section.Section;
import subway.section.SectionDao;
import subway.section.SectionRequest;
import subway.station.Station;
import subway.station.StationDao;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private static final String DUPLICATE_LINE_NAME_MESSAGE = "중복된 노선 이름입니다.";
    private static final String FAIL_DELETE_LINE_MESSAGE = "노선을 삭제할 수 없습니다.";
    private static final String FAIL_DELETE_SECTIONS_MESSAGE = "노선 내 모든 구간 정보를 삭제할 수 없습니다.";

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineService(LineDao lineDao, SectionDao sectionDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public LineResponse save(LineRequest lineRequest) {
        lineRequest.validateLineRequest();
        validateDuplicateLineName(lineRequest.getName());

        long lineId = lineDao.save(lineRequest.toLine());
        sectionDao.save(lineId, lineRequest.toSection());
        return findById(lineId);
    }

    private void validateDuplicateLineName(String name) {
        if (lineDao.checkExistByName(name)) {
            throw new DuplicateLineNameException(DUPLICATE_LINE_NAME_MESSAGE);
        }
    }

    public LineResponse findById(long id) {
        Line line = lineDao.findById(id);
        List<Long> stationIds = line.getStationIds();
        List<Station> stations = stationIds.stream()
                .map(stationDao::findById)
                .collect(Collectors.toList());
        return LineResponse.of(line, stations);
    }

    public List<LineResponse> findAll() {
        List<Line> lines = lineDao.findAll();
        return lines.stream()
                .map(line -> {
                    List<Long> stationIds = line.getStationIds();
                    List<Station> stations = stationIds.stream()
                            .map(stationDao::findById)
                            .collect(Collectors.toList());
                    return LineResponse.of(line, stations);
                })
                .collect(Collectors.toList());
    }

    public void updateLine(long id, LineRequest lineRequest) {
        lineDao.updateLine(id, lineRequest.toLine());
    }

    public void deleteById(long id) {
        if (lineDao.deleteById(id) != 1) {
            throw new FailedDeleteLineException(FAIL_DELETE_LINE_MESSAGE);
        }
        if (sectionDao.deleteAllByLineId(id) <= 0) {
            throw new FailedDeleteLineException(FAIL_DELETE_SECTIONS_MESSAGE);
        }
    }

    public void saveSection(long lineId, SectionRequest sectionRequest) {
        Line line = lineDao.findById(lineId);
        Section newSection = sectionRequest.toSection();

        line.addSection(newSection);
        sectionDao.deleteAllByLineId(lineId);
        sectionDao.insertAllSectionsInLine(line);
    }

    public void deleteStationById(long lineId, long stationId) {
        Line line = lineDao.findById(lineId);
        line.deleteSection(stationId);

        sectionDao.deleteAllByLineId(lineId);
        sectionDao.insertAllSectionsInLine(line);
    }
}
