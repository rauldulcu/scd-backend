package com.utcn.employeeapplication.repository;

import com.utcn.employeeapplication.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findAllByParent(Department department);
}
