package subway.section;

import subway.exception.exceptions.InvalidSectionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Sections {

    private static final long MIN_NECESSARY_SECTION_COUNT = 1;

    private static final String ALREADY_EXIST_STATION_MESSAGE = "두 역 모두 노선에 이미 존재합니다.";
    private static final String NOTHING_EXIST_STATION_MESSAGE = "두 역 모두 노선에 존재하지 않습니다.";
    private static final String ALONE_SECTION_MESSAGE = "구간이 하나이기 때문에 삭제할 수 없습니다.";

    private List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> getSections() {
        return sections;
    }

    public long nextUpStationId(long stationId) {
        return sections.stream()
                .filter(section -> section.getUpStationId() == stationId)
                .findFirst()
                .orElseThrow(InvalidSectionException::new)
                .getDownStationId();
    }

    public void addSection(Section newSection) {
        checkAlreadyExistBothStations(newSection);
        checkExistNothing(newSection);

        updateSectionBasedUp(newSection);
        updateSectionBasedDown(newSection);
        sections.add(newSection);
    }

    private void checkAlreadyExistBothStations(Section section) {
        List<Long> stationIds = getStationIds();
        List<Long> stationIdsInNewSection = Arrays.asList(section.getUpStationId(), section.getDownStationId());
        if (stationIds.containsAll(stationIdsInNewSection)) {
            throw new InvalidSectionException(ALREADY_EXIST_STATION_MESSAGE);
        }
    }

    private void checkExistNothing(Section section) {
        List<Long> stationIds = getStationIds();
        if (!stationIds.contains(section.getUpStationId()) && !stationIds.contains(section.getDownStationId())) {
            throw new InvalidSectionException(NOTHING_EXIST_STATION_MESSAGE);
        }
    }

    public List<Long> getStationIds() {
        List<Long> stations = new ArrayList<>();
        Section nextSection = findFirstSection();

        stations.add(nextSection.getUpStationId());
        while (nextSection != null) {
            stations.add(nextSection.getDownStationId());
            nextSection = getNextSection(nextSection.getDownStationId());
        }
        return stations;
    }

    private Section findFirstSection() {
        List<Long> allDownStationIds = sections.stream()
                .map(Section::getDownStationId)
                .collect(Collectors.toList());
        return sections.stream()
                .filter(section -> !allDownStationIds.contains(section.getUpStationId()))
                .findFirst().get();
    }

    private Section getNextSection(long downStationId) {
        return sections.stream()
                .filter(section -> section.getUpStationId() == downStationId)
                .findFirst()
                .orElse(null);
    }

    private void updateSectionBasedUp(Section newSection) {
        sections.stream()
                .filter(section -> section.getUpStationId() == newSection.getUpStationId())
                .findFirst()
                .ifPresent(section -> replaceToUpdatedSectionBasedUp(section, newSection));
    }

    private void replaceToUpdatedSectionBasedUp(Section section, Section newSection) {
        sections.add(new Section(
                newSection.getDownStationId(), section.getDownStationId(), section.getDistance() - newSection.getDistance()
        ));
        sections.remove(section);
    }

    private void updateSectionBasedDown(Section newSection) {
        sections.stream()
                .filter(section -> section.getDownStationId() == newSection.getDownStationId())
                .findFirst()
                .ifPresent(section -> replaceToUpdatedSectionBasedDown(section, newSection));
    }

    private void replaceToUpdatedSectionBasedDown(Section section, Section newSection) {
        sections.add(new Section(
                section.getUpStationId(), newSection.getUpStationId(), section.getDistance() - newSection.getDistance()
        ));
        sections.remove(section);
    }

    public void deleteSection(long stationId) {
        validateLineContainsOverOneSection();

        Optional<Section> sectionWithUp = sections.stream()
                .filter(section -> section.getUpStationId() == stationId)
                .findFirst();
        Optional<Section> sectionWithDown = sections.stream()
                .filter(section -> section.getDownStationId() == stationId)
                .findFirst();
        if (sectionWithUp.isPresent() && sectionWithDown.isPresent()) {
            int mergedDistance = sectionWithUp.get().getDistance() + sectionWithDown.get().getDistance();
            sections.add(new Section(
                    sectionWithDown.get().getUpStationId(), sectionWithUp.get().getDownStationId(), mergedDistance
            ));
        }
        sectionWithUp.ifPresent(section -> sections.remove(section));
        sectionWithDown.ifPresent(section -> sections.remove(section));
    }

    private void validateLineContainsOverOneSection() {
        if (sections.size() == MIN_NECESSARY_SECTION_COUNT) {
            throw new InvalidSectionException(ALONE_SECTION_MESSAGE);
        }
    }
}
