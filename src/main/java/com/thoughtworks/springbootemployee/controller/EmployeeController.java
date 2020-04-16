package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAll() {
        return employeeService.getAll();
    }

    @GetMapping(params = {"page", "pageSize"})
    public List<Employee> getPaginatedAll(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return employeeService.getAll(page, pageSize);
    }

    @GetMapping(params = "gender")
    public List<Employee> getByGender(@RequestParam String gender) {
        return employeeService.getByGender(gender);
    }

    @GetMapping("/{employeeId}")
    public Employee get(@PathVariable Integer employeeId) {
        return employeeService.get(employeeId);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Employee create(@RequestBody Employee employee) {
        return employeeService.create(employee);
    }

    @DeleteMapping("/{employeeId}")
    public void delete(@PathVariable Integer employeeId) {
        employeeService.delete(employeeId);
    }

    @PutMapping("/{employeeId}")
    public Employee update(@PathVariable Integer employeeId, @RequestBody Employee employee) {
        return employeeService.update(employeeId, employee);
    }
}
