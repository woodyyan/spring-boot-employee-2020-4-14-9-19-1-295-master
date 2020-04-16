package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public List<Company> getAll() {
        return companyService.getAll();
    }

    @GetMapping(params = {"page", "pageSize"})
    public List<Company> getAll(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return companyService.getAll(page, pageSize);
    }

    @GetMapping("/{companyId}")
    public Company get(@PathVariable Integer companyId) {
        return companyService.get(companyId);
    }

    @GetMapping("/{companyId}/employees")
    public List<Employee> getEmployees(@PathVariable Integer companyId) {
        return companyService.getEmployees(companyId);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Company create(@RequestBody Company company) {
        return companyService.create(company);
    }

    @DeleteMapping("/{companyId}")
    public void deleteEmployeesInCompany(@PathVariable Integer companyId) {
        companyService.deleteEmployeesInCompany(companyId);
    }

    @PutMapping("/{companyId}")
    public Company update(@PathVariable Integer companyId, @RequestBody Company company) {
        return companyService.update(companyId, company);
    }
}
