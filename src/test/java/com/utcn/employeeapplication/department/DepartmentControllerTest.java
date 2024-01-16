package com.utcn.employeeapplication.department;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utcn.employeeapplication.controller.DepartmentController;
import com.utcn.employeeapplication.entity.Department;
import com.utcn.employeeapplication.entity.dto.DepartmentDTO;
import com.utcn.employeeapplication.service.DepartmentService;
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

class DepartmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
    }

    @Test
    void getAllDepartments() throws Exception {
        List<Department> departments = generateSampleDepartments();
        when(departmentService.getAllDepartments()).thenReturn(departments);

        mockMvc.perform(get("/api/department"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("IT Department"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("HR Department"));

        verify(departmentService, times(1)).getAllDepartments();
    }

    @Test
    void getDepartmentById() throws Exception {
        Department department = generateSampleDepartments().get(0);
        when(departmentService.getDepartmentById(anyLong())).thenReturn(department);

        mockMvc.perform(get("/api/department/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("IT Department"));

        verify(departmentService, times(1)).getDepartmentById(anyLong());
    }

    @Test
    void createDepartment() throws Exception {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        Department createdDepartment = generateSampleDepartments().get(0);
        when(departmentService.create(any(DepartmentDTO.class))).thenReturn(createdDepartment);

        mockMvc.perform(post("/api/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(departmentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists());

        verify(departmentService, times(1)).create(any(DepartmentDTO.class));
    }

    @Test
    void updateDepartment() throws Exception {
        DepartmentDTO updatedDepartmentDTO = new DepartmentDTO();
        Department updatedDepartment = generateSampleDepartments().get(0);
        when(departmentService.update(anyLong(), any(DepartmentDTO.class))).thenReturn(updatedDepartment);

        mockMvc.perform(put("/api/department/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedDepartmentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists());

        verify(departmentService, times(1)).update(anyLong(), any(DepartmentDTO.class));
    }

    @Test
    void deleteDepartment() throws Exception {
        mockMvc.perform(delete("/api/department/{id}", 1))
                .andExpect(status().isOk());

        verify(departmentService, times(1)).delete(anyLong());
    }

    private List<Department> generateSampleDepartments() {
        Department department1 = new Department(1L, "IT Department", "Nr. 1 department", null);
        Department department2 = new Department(2L, "HR Department", "Nr. 1 department", department1);

        List<Department> departments = new ArrayList<>();
        departments.add(department1);
        departments.add(department2);
        return departments;
    }
}
