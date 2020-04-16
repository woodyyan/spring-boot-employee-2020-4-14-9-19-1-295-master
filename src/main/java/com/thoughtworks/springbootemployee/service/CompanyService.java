package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public List<Company> getAll() {
        return companyRepository.findAll();
    }

    public List<Company> getAll(Integer page, Integer pageSize) {
        return companyRepository.findAll(PageRequest.of(page, pageSize)).getContent();
    }

    public Company get(Integer companyId) {
        return companyRepository.findById(companyId).orElse(null);
    }

    public Company create(Company company) {
        companyRepository.save(company);
        return company;
    }

    public void deleteEmployeesInCompany(Integer companyId) {
        companyRepository.findById(companyId)
                .ifPresent(company -> company.setEmployees(new ArrayList<>()));
    }

    public Company update(Integer companyId, Company companyUpdate) {
        Company company = companyRepository.findById(companyId).orElse(null);

        if (company == null) {
            return null;
        }

        if (companyUpdate.getCompanyName() != null) {
            company.setCompanyName(companyUpdate.getCompanyName());
        }
        if (companyUpdate.getEmployeeNumber() != null) {
            company.setEmployeeNumber(companyUpdate.getEmployeeNumber());
        }
        if (companyUpdate.getEmployees() != null) {
            company.setEmployees(companyUpdate.getEmployees());
        }
        return company;
    }

    public List<Employee> getEmployees(Integer companyId) {
        Company company = companyRepository.findById(companyId).orElse(null);
        if (company == null) {
            return null;
        }
        return company.getEmployees();
    }
}
