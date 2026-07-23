package org.example.zzazo.domain.lecture.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LiberalCategory {
    COMMUNICATION("의사소통"),
    GACHON_VISION("가천비전"),
    AI_BASIC("AI기초"),
    HUMANITIES_AND_ARTS("[공통선택]인간과예술"),
    WORLD_AND_LANGUAGE("[공통선택]세계와언어"),
    SOCIETY_AND_HISTORY("[공통선택]사회와역사"),
    NATURE_AND_SCIENCE("[공통선택]자연과과학"),
    COMMON("일반선택")
    ;

    private final String value;


}
