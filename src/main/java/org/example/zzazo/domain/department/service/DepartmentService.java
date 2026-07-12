package org.example.zzazo.domain.department.service;

import lombok.RequiredArgsConstructor;
import org.example.zzazo.domain.department.dto.DepartmentResponse;
import org.example.zzazo.domain.department.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;


    public DepartmentResponse.DepartmentList getDepartmentList() {

        List<DepartmentResponse.DepartmentList.DepartmentDetail> departmentDetailList = departmentRepository
                .findAll()
                .stream()
                .map(DepartmentResponse.DepartmentList.DepartmentDetail::from)
                .toList();

        return new DepartmentResponse.DepartmentList(departmentDetailList);

    }
}
