package com.utcn.employeeapplication.entity.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Long id;

    private String name;

    private Long departmentId;

    private Long managerId;

    private List<Long> managedEmployees;

    @Email(message = "Not a valid email.")
    private String email;

    private String password;
}
