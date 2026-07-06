package org.example.zzazo.domain.department.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public class DepartmentResponse {
    public record DepartmentList(List<Department> departments) {

        public record Department(
                @Schema(description = "학과ID", example = "1")
                Long departmentId,
                @Schema(description = "학과명", example = "경영학과")
                String departmentName
        ) {

        }
    }
}
