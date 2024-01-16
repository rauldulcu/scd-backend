package com.utcn.employeeapplication.entity.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {
    private Long id;

    private String name;

    private String description;

    private Long parentId;
}
