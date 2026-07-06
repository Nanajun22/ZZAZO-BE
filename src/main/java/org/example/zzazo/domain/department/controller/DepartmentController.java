package org.example.zzazo.domain.department.controller;

import org.example.zzazo.domain.department.controller.docs.DepartmentControllerDocs;
import org.example.zzazo.domain.department.dto.DepartmentResponse;
import org.example.zzazo.global.code.BaseSuccessCode;
import org.example.zzazo.global.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController implements DepartmentControllerDocs {
    @Override
    @GetMapping
    public ApiResponse<DepartmentResponse.DepartmentList> getDepartmentList() {
        return ApiResponse.success(BaseSuccessCode.GENERAL_OK);
    }
}
