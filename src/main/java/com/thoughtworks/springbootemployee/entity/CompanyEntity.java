package com.thoughtworks.springbootemployee.entity;

import com.thoughtworks.springbootemployee.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

//@Entity
//@Table(name = "company")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyEntity {
    private Integer id;
    private String companyName;
    private Integer employeeNumber;
    private List<Employee> employees;
}
