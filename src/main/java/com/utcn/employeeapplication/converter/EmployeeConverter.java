package com.utcn.employeeapplication.converter;

import com.utcn.employeeapplication.entity.Department;
import com.utcn.employeeapplication.entity.Employee;
import com.utcn.employeeapplication.entity.dto.EmployeeDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class EmployeeConverter {

    public static EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        employeeDTO.setDepartmentId(employee.getDepartment().getId());
        employeeDTO.setManagerId(employee.getManager() !=null ? employee.getManager().getId() : null);
        employeeDTO.setManagedEmployees(employee.getManagedEmployees().stream().map(Employee::getId).toList());
        return employeeDTO;
    }

    public static Employee convertToEntity(EmployeeDTO employeeDTO, Department department, Employee manager, List<Employee> managedEmployees) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setDepartment(department);
        employee.setManager(manager);
        employee.setManagedEmployees(managedEmployees);
        return employee;
    }
}
