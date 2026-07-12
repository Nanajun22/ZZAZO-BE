package org.example.zzazo.domain.department.controller;

import lombok.RequiredArgsConstructor;
import org.example.zzazo.domain.department.controller.docs.DepartmentControllerDocs;
import org.example.zzazo.domain.department.dto.DepartmentResponse;
import org.example.zzazo.domain.department.service.DepartmentService;
import org.example.zzazo.global.code.BaseSuccessCode;
import org.example.zzazo.global.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController implements DepartmentControllerDocs {
    private final DepartmentService departmentService;

    @Override
    @GetMapping
    public ApiResponse<DepartmentResponse.DepartmentList> getDepartmentList() {
        DepartmentResponse.DepartmentList departmentList = departmentService.getDepartmentList();
        return ApiResponse.success(BaseSuccessCode.GENERAL_OK,departmentList);
    }
}
