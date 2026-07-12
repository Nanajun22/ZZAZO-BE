package org.example.zzazo.domain.lecture.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LectureClassification {
    MAJOR_REQUIREMENT("전공필수"),
    MAJOR_ELECTIVE("전공선택"),
    LIBERAL_REQUIREMENT("교양필수"),
    LIBERAL_ELECTIVE("교양선택");

    private final String value;
}
