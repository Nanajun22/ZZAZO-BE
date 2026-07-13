package org.example.zzazo.domain.lecture.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.zzazo.domain.lecture.domain.LectureClassification;
import org.example.zzazo.domain.lecture.domain.LiberalCategory;
import org.example.zzazo.domain.lecturegroup.entity.LectureGroup;
import org.example.zzazo.domain.lectureschedule.entity.LectureSchedule;

import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "lecture")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id", nullable = false)
    private Long id;

    @Column(name = "lecture_name", nullable = false)
    private String name;

    @Column(name = "credit", nullable = false)
    private int credit;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_classification",nullable = false,length = 50)
    private LectureClassification lectureClassification;

    @Enumerated(EnumType.STRING)
    @Column(name = "liberal_category",nullable = true,length = 50)
    private LiberalCategory liberalCategory;

    @Column(name = "semester", nullable = false)
    private int semester;

    @Column(name = "lecture_year", nullable = false)
    private int year;

    @Column(name = "grade", nullable = false)
    private int grade;

    @Column(name = "classroom",nullable = true,length = 50)
    private String classroom;

    @Column(name = "professor",nullable = true,length = 50)
    private String professor;

    @Column(name = "course_code",nullable = false)
    private String courseCode;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_group_id",nullable = false)
    private LectureGroup lectureGroup;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LectureSchedule> lectureSchedules = new ArrayList<>();


}
