package com.utcn.employeeapplication.employee;

import com.utcn.employeeapplication.entity.Department;
import com.utcn.employeeapplication.entity.Employee;
import com.utcn.employeeapplication.entity.dto.EmployeeDTO;
import com.utcn.employeeapplication.repository.DepartmentRepository;
import com.utcn.employeeapplication.repository.EmployeeRepository;
import com.utcn.employeeapplication.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private List<Employee> sampleEmployees;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        sampleEmployees = generateSampleEmployees();
    }

    @Test
    void createEmployee() throws Exception {
        // Arrange
        EmployeeDTO employeeDTO = new EmployeeDTO(); // Initialize with necessary data
        Department department = new Department(1L, "IT Department", "Nr. 1 department", null);
        when(departmentRepository.findById(any())).thenReturn(Optional.of(department));
        when(employeeRepository.findById(any())).thenReturn(Optional.empty());
        when(employeeRepository.save(any())).thenReturn(sampleEmployees.get(0));

        // Act
        Employee createdEmployee = employeeService.create(employeeDTO);

        // Assert
        assertNotNull(createdEmployee);
        verify(departmentRepository, times(1)).findById(any());
        verify(employeeRepository, times(1)).findById(any());
        verify(employeeRepository, times(1)).save(any());
    }

    @Test
    void getAllEmployees() {
        // Arrange
        when(employeeRepository.findAll()).thenReturn(sampleEmployees);

        // Act
        List<Employee> result = employeeService.getAllEmployees();

        // Assert
        assertNotNull(result);
        assertEquals(sampleEmployees, result);
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void getEmployeeById() {
        // Arrange
        Employee sampleEmployee = sampleEmployees.get(0);
        when(employeeRepository.findById(any())).thenReturn(Optional.of(sampleEmployee));

        // Act
        Employee result = employeeService.getEmployeeById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(sampleEmployee, result);
        verify(employeeRepository, times(1)).findById(any());
    }

    @Test
    void getAllEmployeesPerDepartment() {
        // Arrange
        when(employeeRepository.findAllByDepartment_Id(any())).thenReturn(sampleEmployees);

        // Act
        List<Employee> result = employeeService.getAllEmployeesPerDepartment(1L);

        // Assert
        assertNotNull(result);
        assertEquals(sampleEmployees, result);
        verify(employeeRepository, times(1)).findAllByDepartment_Id(any());
    }

    @Test
    void getAllManagersPerDepartment() {
        // Arrange
        List<Employee> managers = sampleEmployees.subList(0, 1);
        when(employeeRepository.findAllByDepartment_IdAndManagedEmployeesNotNull(any())).thenReturn(managers);

        // Act
        List<Employee> result = employeeService.getAllManagersPerDepartment(1L);

        // Assert
        assertNotNull(result);
        assertEquals(managers, result);
        verify(employeeRepository, times(1)).findAllByDepartment_IdAndManagedEmployeesNotNull(any());
    }

    @Test
    void deleteEmployee() throws Exception {
        // Arrange
        Employee employee = sampleEmployees.get(0);
        when(employeeRepository.findById(any())).thenReturn(Optional.of(employee));

        // Act
        assertDoesNotThrow(() -> employeeService.delete(1L));

        // Assert
        verify(employeeRepository, times(1)).findById(any());
        verify(employeeRepository, times(1)).delete(any());
    }

    public static List<Employee> generateSampleEmployees() {
        List<Employee> employees = new ArrayList<>();

        Department department1 = new Department(1L, "IT Department", "Nr. 1 department", null);
        Department department2 = new Department(2L, "HR Department", "Nr. 1 department", department1);

        Employee employee1 = new Employee(1L, "John Doe", department1, null, new ArrayList<>(), "john.doe@example.com", "1234");
        Employee employee2 = new Employee(2L, "Jane Smith", department1, employee1, new ArrayList<>(), "jane.smith@example.com", "1234");
        employee1.getManagedEmployees().add(employee2);
        Employee employee3 = new Employee(3L, "Bob Johnson", department2, null, new ArrayList<>(), "bob.johnson@example.com", "1234");

        employees.add(employee1);
        employees.add(employee2);
        employees.add(employee3);

        return employees;
    }
}
