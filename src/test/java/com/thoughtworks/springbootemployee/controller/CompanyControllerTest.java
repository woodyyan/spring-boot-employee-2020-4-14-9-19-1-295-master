package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.model.ParkingBoy;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CompanyControllerTest {

    @Autowired
    private CompanyController companyController;

    @MockBean
    private CompanyRepository companyRepository;

    @Before
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(companyController);

        List<Employee> employeesInSpring = new ArrayList<>();
        employeesInSpring.add(new Employee(10, 1, "spring1", 20, "Male", 1000));
        employeesInSpring.add(new Employee(11, 1, "spring2", 19, "Male", 2000));
        employeesInSpring.add(new Employee(12, 1, "spring3", 15, "Male", 3000));

        List<Employee> employeesInBoot = new ArrayList<>();
        employeesInBoot.add(new Employee(13, 1, "boot1", 16, "Male", 4000));
        employeesInBoot.add(new Employee(14, 1, "boot2", 15, "Male", 5000));

        Company company0 = new Company(0, "spring", 3, employeesInSpring);
        Company company1 = new Company(1, "boot", 2, employeesInBoot);

        List<Company> companies = new ArrayList<>();
        companies.add(company0);
        companies.add(company1);

        Mockito.when(companyRepository.findAll())
            .thenReturn(companies);

        List<Company> secondPageCompanies = new ArrayList<>();
        secondPageCompanies.add(company1);
        Page<Company> pagedCompanies = new PageImpl<>(secondPageCompanies);
        Mockito.when(companyRepository.findAll(any(Pageable.class)))
            .thenReturn(pagedCompanies);

        Mockito.when(companyRepository.findById(1))
            .thenReturn(Optional.of(company1));
    }

    @Test
    public void should_return_all_companies_when_get_all() {
        MockMvcResponse response = RestAssuredMockMvc.given().contentType(ContentType.JSON)
            .get("/companies");

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        Assert.assertEquals(2, response.jsonPath().getList("$").size());

        Assert.assertEquals(0, response.jsonPath().getLong("[0].id"));
        Assert.assertEquals("spring", response.jsonPath().get("[0].companyName"));
        Assert.assertEquals(3, response.jsonPath().getLong("[0].employeeNumber"));
        Assert.assertEquals(3, response.jsonPath().getList("[0].employees").size());

        Assert.assertEquals(10, response.jsonPath().getLong("[0].employees[0].id"));
        Assert.assertEquals("spring1", response.jsonPath().get("[0].employees[0].name"));
        Assert.assertEquals(20, response.jsonPath().getLong("[0].employees[0].age"));
        Assert.assertEquals("Male", response.jsonPath().get("[0].employees[0].gender"));
        Assert.assertEquals(1000, response.jsonPath().getLong("[0].employees[0].salary"));
    }

    @Test
    public void should_return_correct_companies_when_get_all_with_page() {
        MockMvcResponse response = RestAssuredMockMvc.given().contentType(ContentType.JSON)
            .queryParam("page", 2)
            .queryParam("pageSize", 1)
            .get("/companies");

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        Assert.assertEquals(1, response.jsonPath().getList("$").size());

        Assert.assertEquals(1, response.jsonPath().getLong("[0].id"));
        Assert.assertEquals("boot", response.jsonPath().get("[0].companyName"));
        Assert.assertEquals(2, response.jsonPath().getLong("[0].employeeNumber"));
        Assert.assertEquals(2, response.jsonPath().getList("[0].employees").size());

        Assert.assertEquals(13, response.jsonPath().getLong("[0].employees[0].id"));
        Assert.assertEquals("boot1", response.jsonPath().get("[0].employees[0].name"));
        Assert.assertEquals(16, response.jsonPath().getLong("[0].employees[0].age"));
        Assert.assertEquals("Male", response.jsonPath().get("[0].employees[0].gender"));
        Assert.assertEquals(4000, response.jsonPath().getLong("[0].employees[0].salary"));
    }

    @Test
    public void should_return_company_1_when_get_1() {
        MockMvcResponse response = RestAssuredMockMvc.given().contentType(ContentType.JSON)
            .get("/companies/1");

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        Assert.assertEquals(1, response.jsonPath().getLong("id"));
        Assert.assertEquals("boot", response.jsonPath().get("companyName"));
        Assert.assertEquals(2, response.jsonPath().getLong("employeeNumber"));
        Assert.assertEquals(2, response.jsonPath().getList("employees").size());

        Assert.assertEquals(13, response.jsonPath().getLong("employees[0].id"));
        Assert.assertEquals("boot1", response.jsonPath().get("employees[0].name"));
        Assert.assertEquals(16, response.jsonPath().getLong("employees[0].age"));
        Assert.assertEquals("Male", response.jsonPath().get("employees[0].gender"));
        Assert.assertEquals(4000, response.jsonPath().getLong("employees[0].salary"));
    }

    @Test
    public void should_return_employees_when_get_company_employees() {
        MockMvcResponse response = RestAssuredMockMvc.given().contentType(ContentType.JSON)
            .get("/companies/1/employees");

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        Assert.assertEquals(2, response.jsonPath().getList("$").size());

        Assert.assertEquals(13, response.jsonPath().getLong("[0].id"));
        Assert.assertEquals("boot1", response.jsonPath().get("[0].name"));
        Assert.assertEquals(16, response.jsonPath().getLong("[0].age"));
        Assert.assertEquals("Male", response.jsonPath().get("[0].gender"));
        Assert.assertEquals(4000, response.jsonPath().getLong("[0].salary"));
    }

    @Test
    public void should_return_correct_company_when_create() {
        MockMvcResponse response = RestAssuredMockMvc.given().contentType(ContentType.JSON)
            .body("{" +
                "\"id\": 10," +
                "\"companyName\": \"Test\"," +
                "\"employeeNumber\": 0," +
                "\"employees\": []" +
                "}")
            .post("/companies");

        Assert.assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());

        Assert.assertEquals(10, response.jsonPath().getLong("id"));
        Assert.assertEquals("Test", response.jsonPath().get("companyName"));
        Assert.assertEquals(0, response.jsonPath().getLong("employeeNumber"));
        Assert.assertEquals(0, response.jsonPath().getList("employees").size());
    }

    @Test
    public void should_return_200_when_delete() {
        MockMvcResponse response = RestAssuredMockMvc.given().contentType(ContentType.JSON)
            .delete("/companies/1");

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void should_return_correct_company_when_update() {
        MockMvcResponse response = RestAssuredMockMvc.given().contentType(ContentType.JSON)
            .body("{" +
                "\"companyName\": \"New name\"" +
                "}")
            .put("/companies/1");

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        Assert.assertEquals(1, response.jsonPath().getLong("id"));
        Assert.assertEquals("New name", response.jsonPath().get("companyName"));
        Assert.assertEquals(2, response.jsonPath().getLong("employeeNumber"));
        Assert.assertEquals(2, response.jsonPath().getList("employees").size());

        Assert.assertEquals(13, response.jsonPath().getLong("employees[0].id"));
        Assert.assertEquals("boot1", response.jsonPath().get("employees[0].name"));
        Assert.assertEquals(16, response.jsonPath().getLong("employees[0].age"));
        Assert.assertEquals("Male", response.jsonPath().get("employees[0].gender"));
        Assert.assertEquals(4000, response.jsonPath().getLong("employees[0].salary"));
    }
}
