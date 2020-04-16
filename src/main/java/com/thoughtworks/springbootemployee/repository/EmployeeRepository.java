package com.thoughtworks.springbootemployee.repository;

import com.thoughtworks.springbootemployee.model.Employee;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepository {
    private List<Employee> employees = new ArrayList<>();

    @PostConstruct
    private void addEmployees() {
        employees.add(new Employee(0, "Xiaoming", 20, "Male", 0));
        employees.add(new Employee(1, "Xiaohong", 19, "Male", 0));
        employees.add(new Employee(2, "Xiaozhi", 15, "Male", 0));
        employees.add(new Employee(3, "Xiaogang", 16, "Male", 0));
        employees.add(new Employee(4, "Xiaoxia", 15, "Male", 0));
    }

    public List<Employee> findAll() {
        return employees;
    }

    public List<Employee> findAll(Integer page, Integer pageSize) {
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min((fromIndex + pageSize), employees.size());
        return employees.subList(fromIndex, toIndex);
    }

    public Optional<Employee> findById(Integer employeeId) {
        return employees.stream()
                .filter(employee -> employee.getId().equals(employeeId))
                .findFirst();
    }

    public void save(Employee employee) {
        employees.add(employee);
    }

    public void delete(Employee employee) {
        employees.remove(employee);
    }

    public List<Employee> findByGender(String gender) {
        return employees.stream()
                .filter(employee -> employee.getGender().toLowerCase().equals(gender.toLowerCase()))
                .collect(Collectors.toList());
    }
}
