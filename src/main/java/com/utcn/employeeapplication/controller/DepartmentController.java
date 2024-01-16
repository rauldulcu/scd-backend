package com.utcn.employeeapplication.controller;

import com.utcn.employeeapplication.converter.DepartmentConverter;
import com.utcn.employeeapplication.entity.Department;
import com.utcn.employeeapplication.entity.dto.DepartmentDTO;
import com.utcn.employeeapplication.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public List<DepartmentDTO> getAllDepartments() {
        return convertToDTOList(departmentService.getAllDepartments());
    }

    @GetMapping("/{id}")
    public DepartmentDTO getDepartmentById(@PathVariable Long id) {
        return convertToDTO(departmentService.getDepartmentById(id));
    }

    @PostMapping
    public DepartmentDTO createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        return convertToDTO(departmentService.create(departmentDTO));
    }

    @PutMapping("/{id}")
    public DepartmentDTO updateDepartment(@PathVariable Long id, @RequestBody DepartmentDTO departmentDTO) throws Exception {
        return convertToDTO(departmentService.update(id, departmentDTO));
    }

    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable Long id) throws Exception {
        departmentService.delete(id);
    }

    private DepartmentDTO convertToDTO(Department department) {
        return DepartmentConverter.convertToDTO(department);
    }

    private List<DepartmentDTO> convertToDTOList(List<Department> departments) {
        return departments.stream().map(DepartmentConverter::convertToDTO).toList();
    }
}
