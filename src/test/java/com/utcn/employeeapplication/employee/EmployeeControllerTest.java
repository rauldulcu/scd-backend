package com.utcn.employeeapplication.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utcn.employeeapplication.controller.EmployeeController;
import com.utcn.employeeapplication.entity.Department;
import com.utcn.employeeapplication.entity.Employee;
import com.utcn.employeeapplication.entity.dto.EmployeeDTO;
import com.utcn.employeeapplication.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    void getAllEmployees() throws Exception {
        List<Employee> sampleEmployees = generateSampleEmployees();
        when(employeeService.getAllEmployees()).thenReturn(sampleEmployees);

        mockMvc.perform(get("/api/employee"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1)) // Change to the actual property name
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].id").value(2)) // Change to the actual property name
                .andExpect(jsonPath("$[1].name").value("Jane Smith"))
                .andExpect(jsonPath("$[2].id").value(3)) // Change to the actual property name
                .andExpect(jsonPath("$[2].name").value("Bob Johnson"));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void getEmployeeById() throws Exception {
        Employee sampleEmployee = generateSampleEmployees().get(0);
        when(employeeService.getEmployeeById(anyLong())).thenReturn(sampleEmployee);

        mockMvc.perform(get("/api/employee/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(employeeService, times(1)).getEmployeeById(anyLong());
    }

    @Test
    void getAllEmployeesPerDepartment() throws Exception {
        List<Employee> employees = generateSampleEmployees();
        when(employeeService.getAllEmployeesPerDepartment(anyLong())).thenReturn(employees);

        mockMvc.perform(get("/api/employee/department/{departmentId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"));

        verify(employeeService, times(1)).getAllEmployeesPerDepartment(anyLong());
    }

    @Test
    void getAllManagersPerDepartment() throws Exception {
        List<Employee> managers = generateSampleEmployees().subList(0, 1);
        when(employeeService.getAllManagersPerDepartment(anyLong())).thenReturn(managers);

        mockMvc.perform(get("/api/employee/department/{departmentId}/managers", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"));

        verify(employeeService, times(1)).getAllManagersPerDepartment(anyLong());
    }

    @Test
    void createEmployee() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO(); // Initialize with necessary data
        Employee createdEmployee = generateSampleEmployees().get(0); // Use a sample employee for verification

        when(employeeService.create(any(EmployeeDTO.class))).thenReturn(createdEmployee);

        mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(createdEmployee.getId()))
                .andExpect(jsonPath("$.departmentId").value(createdEmployee.getDepartment().getId()))
                .andExpect(jsonPath("$.name").value(createdEmployee.getName()))
                .andExpect(jsonPath("$.email").value(createdEmployee.getEmail()));

        verify(employeeService, times(1)).create(any(EmployeeDTO.class));
    }

    @Test
    void updateEmployee() throws Exception {
        EmployeeDTO updatedEmployeeDTO = new EmployeeDTO(); // Initialize with necessary data
        Employee updatedEmployee = generateSampleEmployees().get(0); // Use a sample employee for verification

        when(employeeService.update(any(EmployeeDTO.class))).thenReturn(updatedEmployee);

        mockMvc.perform(put("/api/employee/{id}", updatedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedEmployeeDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(updatedEmployee.getId()))
                .andExpect(jsonPath("$.departmentId").value(updatedEmployee.getDepartment().getId()))
                .andExpect(jsonPath("$.name").value(updatedEmployee.getName()))
                .andExpect(jsonPath("$.email").value(updatedEmployee.getEmail()));

        verify(employeeService, times(1)).update(any(EmployeeDTO.class));
    }

    @Test
    void deleteEmployee() throws Exception {
        mockMvc.perform(delete("/api/employee/{id}", 1))
                .andExpect(status().isOk());

        verify(employeeService, times(1)).delete(anyLong());
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
