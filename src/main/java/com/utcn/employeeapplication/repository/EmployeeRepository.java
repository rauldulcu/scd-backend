package com.utcn.employeeapplication.repository;

import com.utcn.employeeapplication.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

    List<Employee> findAllByDepartment_Id(Long departmentId);

    List<Employee> findAllByDepartment_IdAndManagedEmployeesNotNull(Long departmentId);

    Optional<Employee> findByEmail(String email);
}
