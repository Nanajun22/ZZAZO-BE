package org.example.zzazo.domain.timetable.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import org.example.zzazo.domain.user.entity.User;
import org.example.zzazo.global.common.Week;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "timetable")
@Getter
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timetable_id")
    private Long timetableId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "candidate_name", nullable = false, length = 50)
    private String candidateName;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Convert(converter = WeekListConverter.class)
    @Column(name = "preferred_free_days", length = 50)
    private List<Week> preferredFreeDays;

    @Column(name = "total_credits", nullable = false)
    private int totalCredits;

    @Column(name = "target_credits", nullable = false)
    private int targetCredits;

    @Column(name = "grade", nullable = false)
    private int grade;

    @Column(name = "semester", nullable = false)
    private int semester;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    protected Timetable() {
    }

    public Timetable(
            User user,
            String candidateName,
            Long departmentId,
            List<Week> preferredFreeDays,
            int totalCredits,
            int targetCredits,
            int grade,
            int semester
    ) {
        this.user = user;
        this.candidateName = candidateName;
        this.departmentId = departmentId;
        this.preferredFreeDays = preferredFreeDays;
        this.totalCredits = totalCredits;
        this.targetCredits = targetCredits;
        this.grade = grade;
        this.semester = semester;
    }

}
