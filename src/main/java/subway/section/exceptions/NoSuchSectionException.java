package subway.section.exceptions;

import java.util.NoSuchElementException;

public class NoSuchSectionException extends NoSuchElementException {
    private static final String MESSAGE_FORMAT = "id:%d section을 찾을 수 없습니다.";

    public NoSuchSectionException(Long sectionId) {
        super(String.format(MESSAGE_FORMAT, sectionId));
    }
}
