package com.utcn.employeeapplication.department;

import com.utcn.employeeapplication.entity.Department;
import com.utcn.employeeapplication.entity.dto.DepartmentDTO;
import com.utcn.employeeapplication.repository.DepartmentRepository;
import com.utcn.employeeapplication.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private List<Department> sampleDepartments;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        sampleDepartments = generateSampleDepartments();
    }

    @Test
    void createDepartment() {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        when(departmentRepository.findById(any())).thenReturn(Optional.empty());
        when(departmentRepository.save(any())).thenReturn(sampleDepartments.get(0));

        Department createdDepartment = departmentService.create(departmentDTO);

        assertNotNull(createdDepartment);
        verify(departmentRepository, times(1)).findById(any());
        verify(departmentRepository, times(1)).save(any());
    }

    @Test
    void getAllDepartments() {
        when(departmentRepository.findAll()).thenReturn(sampleDepartments);

        List<Department> result = departmentService.getAllDepartments();

        assertNotNull(result);
        assertEquals(sampleDepartments, result);
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void getDepartmentById() {
        Department sampleDepartment = sampleDepartments.get(0);
        when(departmentRepository.findById(any())).thenReturn(Optional.of(sampleDepartment));

        Department result = departmentService.getDepartmentById(1L);

        assertNotNull(result);
        assertEquals(sampleDepartment, result);
        verify(departmentRepository, times(1)).findById(any());
    }

    @Test
    void deleteDepartment() throws Exception {
        Department department = sampleDepartments.get(0);
        when(departmentRepository.findById(any())).thenReturn(Optional.of(department));
        when(departmentRepository.findAllByParent(any())).thenReturn(sampleDepartments.subList(1, 2));

        assertDoesNotThrow(() -> departmentService.delete(1L));

        verify(departmentRepository, times(1)).findById(any());
        verify(departmentRepository, times(1)).findAllByParent(any());
        verify(departmentRepository, times(1)).delete(any());
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
