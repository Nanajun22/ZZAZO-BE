package org.example.zzazo.domain.lecture.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LiberalCategory {
    COMMUNICATION("의사소통"),
    GACHON_VISION("가천비전"),
    AI_BASIC("AI기초");

    private final String value;


}
