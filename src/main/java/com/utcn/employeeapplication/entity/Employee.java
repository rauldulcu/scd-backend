package com.utcn.employeeapplication.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private Department department;

    @ManyToOne
    private Employee manager;

    @OneToMany
    private List<Employee> managedEmployees;

    @Email(message = "Not a valid email.")
    private String email;

    private String password;
}
