package org.example.zzazo.domain.curriculum.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.zzazo.domain.department.entity.Department;
import org.example.zzazo.domain.lecture.entity.Lecture;

@Entity @Table(name = "curriculum")
@Getter
@NoArgsConstructor
public class Curriculum {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curriculum_id", nullable = false)
    private Long id;

    @Column(name = "grade", nullable = false)
    private int grade;

    @Column(name = "requirement", nullable = false)
    private Boolean isRequired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id" ,nullable = false)
    private Lecture lecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id",nullable = false)
    private Department department;

}
