package org.example.zzazo.domain.lecturegroup.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.zzazo.global.entity.BaseTimeEntity;

@Entity @Table(name = "lecture_group")
@Getter
public class LectureGroup {

    @Column(name = "lecture_group_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lecture_group_name")
    private String name;


}
