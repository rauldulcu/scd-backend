package com.utcn.employeeapplication.converter;

import com.utcn.employeeapplication.entity.Department;
import com.utcn.employeeapplication.entity.dto.DepartmentDTO;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

public class DepartmentConverter {

    public static DepartmentDTO convertToDTO(Department department) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        BeanUtils.copyProperties(department, departmentDTO);
        departmentDTO.setParentId(department.getParent() != null ? department.getParent().getId() : null);
        return departmentDTO;
    }

    public static Department convertToEntity(DepartmentDTO departmentDTO, Department parentDepartment) {
        Department department = new Department();
        BeanUtils.copyProperties(departmentDTO, department);
        department.setParent(parentDepartment);
        return department;
    }
}

