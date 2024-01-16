package com.utcn.employeeapplication.service;

import com.utcn.employeeapplication.converter.EmployeeConverter;
import com.utcn.employeeapplication.entity.Department;
import com.utcn.employeeapplication.entity.Employee;
import com.utcn.employeeapplication.entity.dto.EmployeeDTO;
import com.utcn.employeeapplication.repository.DepartmentRepository;
import com.utcn.employeeapplication.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional
    public Employee create(EmployeeDTO employeeDTO) throws Exception {
        Optional<Department> department = departmentRepository.findById(employeeDTO.getDepartmentId());
        if (department.isEmpty()) throw new Exception("Department not found.");

        Employee manager = employeeRepository.findById(employeeDTO.getManagerId()).orElse(null);
        Employee employee = EmployeeConverter.convertToEntity(employeeDTO, department.get(), manager, new ArrayList<>());
//        employee.setPassword(passwordEncoder().encode(employee.getPassword()));
        if (manager != null)
            manager.getManagedEmployees().add(employee);
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        return employee;
    }

    public List<Employee> getAllEmployeesPerDepartment(Long departmentId) {
        return employeeRepository.findAllByDepartment_Id(departmentId);
    }

    public List<Employee> getAllManagersPerDepartment(Long departmentId) {
        return employeeRepository.findAllByDepartment_IdAndManagedEmployeesNotNull(departmentId);
    }

    @Transactional
    public Employee update(EmployeeDTO updatedEmployeeDTO) throws Exception {
        Optional<Employee> existingEmployeeOptional = employeeRepository.findById(updatedEmployeeDTO.getId());
        if (existingEmployeeOptional.isEmpty()) throw new Exception("Employee not found.");

        Optional<Department> updatedDepartment = departmentRepository.findById(updatedEmployeeDTO.getDepartmentId());
        if (updatedDepartment.isEmpty()) throw new Exception("Department not found.");

        Employee existingEmployee = existingEmployeeOptional.get();
        Employee updatedManager = employeeRepository.findById(updatedEmployeeDTO.getManagerId()).orElse(null);
        Employee updatedEmployee = EmployeeConverter.convertToEntity(updatedEmployeeDTO, updatedDepartment.get(), updatedManager, existingEmployee.getManagedEmployees());

        existingEmployee.setName(updatedEmployee.getName());
        existingEmployee.setDepartment(updatedEmployee.getDepartment());
        if (existingEmployee.getManager() != null && updatedEmployee.getManager() != null)
            if (!existingEmployee.getManager().equals(updatedEmployee.getManager())) {
                employeeRepository.findById(existingEmployee.getManager().getId()).ifPresent(manager -> manager.getManagedEmployees().remove(existingEmployee));
                employeeRepository.findById(updatedEmployee.getManager().getId()).ifPresent(manager -> manager.getManagedEmployees().add(updatedEmployee));
                existingEmployee.setManager(updatedEmployee.getManager());
            }
        existingEmployee.setManagedEmployees(updatedEmployee.getManagedEmployees());
        existingEmployee.setEmail(updatedEmployee.getEmail());
//        if (!passwordEncoder().matches(updatedEmployee.getPassword(), existingEmployee.getPassword()))
//            existingEmployee.setPassword(passwordEncoder().encode(updatedEmployee.getPassword()));

        return employeeRepository.save(existingEmployee);
    }

    public void delete(Long id) throws Exception {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isEmpty()) throw new Exception("Employee not found.");
        Employee employee = employeeOptional.get();

        if (employee.getManager() != null)
            employeeRepository.findById(employee.getManager().getId()).ifPresent(manager -> manager.getManagedEmployees().remove(employee));
        if (!employee.getManagedEmployees().isEmpty())
            employee.getManagedEmployees().forEach(managedEmployee -> managedEmployee.setManager(null));
        employeeRepository.delete(employee);
    }

//    private PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(11);
//    }
}
