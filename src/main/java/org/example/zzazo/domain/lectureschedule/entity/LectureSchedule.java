package org.example.zzazo.domain.lectureschedule.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.zzazo.domain.lecture.entity.Lecture;
import org.example.zzazo.global.common.Week;

import java.time.LocalTime;


@Entity @Table(name = "lecture_schedule")
@Getter
@NoArgsConstructor
public class LectureSchedule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_schedule_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week",nullable = false, length = 10)
    private Week dayOfWeek;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

}
