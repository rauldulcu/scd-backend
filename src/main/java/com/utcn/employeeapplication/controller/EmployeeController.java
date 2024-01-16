package com.utcn.employeeapplication.controller;

import com.utcn.employeeapplication.entity.Employee;
import com.utcn.employeeapplication.converter.EmployeeConverter;
import com.utcn.employeeapplication.entity.dto.EmployeeDTO;
import com.utcn.employeeapplication.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDTO create(@RequestBody EmployeeDTO employeeDTO) throws Exception {
        Employee employee = employeeService.create(employeeDTO);
        return convertToDTO(employee);
    }

    @GetMapping
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return convertToDTOList(employees);
    }

    @GetMapping("/{id}")
    public EmployeeDTO getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return convertToDTO(employee);
    }

    @GetMapping("/department/{departmentId}")
    public List<EmployeeDTO> getAllEmployeesPerDepartment(@PathVariable Long departmentId) {
        List<Employee> employees = employeeService.getAllEmployeesPerDepartment(departmentId);
        return convertToDTOList(employees);
    }

    @GetMapping("/department/{departmentId}/managers")
    public List<EmployeeDTO> getAllManagersPerDepartment(@PathVariable Long departmentId) {
        List<Employee> employees = employeeService.getAllManagersPerDepartment(departmentId);
        return convertToDTOList(employees);
    }

    @PutMapping("/{id}")
    public EmployeeDTO updateEmployee(@RequestBody EmployeeDTO employeeDTO) throws Exception {
        Employee updatedEmployee = employeeService.update(employeeDTO);
        return convertToDTO(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployee(@PathVariable Long id) throws Exception {
        employeeService.delete(id);
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        return EmployeeConverter.convertToDTO(employee);
    }

    private List<EmployeeDTO> convertToDTOList(List<Employee> employees) {
        return employees.stream().map(EmployeeConverter::convertToDTO).toList();
    }
}
