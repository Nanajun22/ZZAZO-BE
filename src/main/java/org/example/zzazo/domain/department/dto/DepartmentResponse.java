package org.example.zzazo.domain.department.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.zzazo.domain.department.entity.Department;

import java.util.List;

public class DepartmentResponse {
    public record DepartmentList(
            @Schema(description = "학과 목록")
            List<DepartmentDetail> departments
    ) {
        public record DepartmentDetail(
                @Schema(description = "학과ID", example = "1")
                Long departmentId,
                @Schema(description = "학과명", example = "경영학과")
                String departmentName
        ) {

            public static DepartmentDetail from(Department department) {
                return new DepartmentDetail(department.getId(), department.getName());
            }

        }
    }
}
