package com.utcn.employeeapplication.service;

import com.utcn.employeeapplication.converter.DepartmentConverter;
import com.utcn.employeeapplication.entity.Department;
import com.utcn.employeeapplication.entity.dto.DepartmentDTO;
import com.utcn.employeeapplication.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public Department create(DepartmentDTO departmentDTO) {
        Department parentDepartment = departmentRepository.findById(departmentDTO.getParentId()).orElse(null);
        Department department = DepartmentConverter.convertToEntity(departmentDTO, parentDepartment);
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    @Transactional
    public Department update(Long id, DepartmentDTO updatedDepartmentDTO) throws Exception {
        Optional<Department> existingDepartmentOptional = departmentRepository.findById(id);
        if (existingDepartmentOptional.isEmpty()) throw new Exception("Department not found.");
        Department existingDepartment = existingDepartmentOptional.get();

        Department parentDepartment = departmentRepository.findById(updatedDepartmentDTO.getParentId()).orElse(null);
        Department updatedDepartment = DepartmentConverter.convertToEntity(updatedDepartmentDTO, parentDepartment);

        existingDepartment.setName(updatedDepartment.getName());
        existingDepartment.setDescription(updatedDepartment.getDescription());
        existingDepartment.setParent(updatedDepartment.getParent());
        return departmentRepository.save(existingDepartment);
    }

    public void delete(Long id) throws Exception {
        Optional<Department> department = departmentRepository.findById(id);
        if (department.isEmpty()) throw new Exception("Department not found.");
        departmentRepository.findAllByParent(department.get()).forEach(childDepartment -> childDepartment.setParent(null));
        departmentRepository.delete(department.get());
    }
}