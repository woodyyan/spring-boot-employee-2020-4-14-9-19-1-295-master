package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
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
public class EmployeeControllerTest {

    @Autowired
    private EmployeeController employeeController;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Before
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(employeeController);

        Employee employee0 = new Employee(0, 1, "Xiaoming", 20, "Male", 0);
        Employee employee1 = new Employee(1, 1, "Xiaohong", 19, "Male", 0);
        Employee employee2 = new Employee(2, 2, "Xiaozhi", 15, "Male", 0);
        Employee employee3 = new Employee(3, 1, "Xiaogang", 16, "Male", 0);
        Employee employee4 = new Employee(4, 1, "Xiaoxia", 15, "Male", 0);

        List<Employee> employees = new ArrayList<>();
        employees.add(employee0);
        employees.add(employee1);
        employees.add(employee2);
        employees.add(employee3);
        employees.add(employee4);

        Mockito.when(employeeRepository.findAll())
            .thenReturn(employees);

        List<Employee> subEmployees = new ArrayList<>();
        subEmployees.add(employee3);
        subEmployees.add(employee4);
        Page<Employee> pagedEmployees = new PageImpl<>(subEmployees);
        Mockito.when(employeeRepository.findAll(any(Pageable.class)))
            .thenReturn(pagedEmployees);

        Mockito.when(employeeRepository.findById(1))
            .thenReturn(Optional.of(employee1));

        Mockito.when(employeeRepository.findByGender("male"))
            .thenReturn(employees);
    }

    @Test
    public void should_return_all_employees_when_get_all() {
        MockMvcResponse response = RestAssuredMockMvc.given().contentType(ContentType.JSON)
            .get("/employees");

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        Assert.assertEquals(5, response.jsonPath().getList("$").size());

        Assert.assertEquals(0, response.jsonPath().getLong("[0].id"));
        Assert.assertEquals("Xiaoming", response.jsonPath().get("[0].name"));
        Assert.assertEquals(20, response.jsonPath().getLong("[0].age"));
        Assert.assertEquals("Male", response.jsonPath().get("[0].gender"));
        Assert.assertEquals(0, response.jsonPath().getLong("[0].salary"));
    }

    @Test
    public void should_return_correct_employee_when_get_all_with_page() {
        MockMvcResponse response = RestAssuredMockMvc.given().contentType(ContentType.JSON)
            .queryParam("page", 2)
            .queryParam("pageSize", 3)
            .get("/employees");

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        Assert.assertEquals(2, response.jsonPath().getList("$").size());

        Assert.assertEquals(3, response.jsonPath().getLong("[0].id"));
        Assert.assertEquals("Xiaogang", response.jsonPath().get("[0].name"));
        Assert.assertEquals(16, response.jsonPath().getLong("[0].age"));
        Assert.assertEquals("Male", response.jsonPath().get("[0].gender"));
        Assert.assertEquals(0, response.jsonPath().getLong("[0].salary"));

        Assert.assertEquals(4, response.jsonPath().getLong("[1].id"));
        Assert.assertEquals("Xiaoxia", response.jsonPath().get("[1].name"));
        Assert.assertEquals(15, response.jsonPath().getLong("[1].age"));
        Assert.assertEquals("Male", response.jsonPath().get("[1].gender"));
        Assert.assertEquals(0, response.jsonPath().getLong("[1].salary"));
    }

    @Test
    public void should_return_correct_employees_when_get_by_gender() {
        MockMvcResponse response = RestAssuredMockMvc.given().contentType(ContentType.JSON)
            .queryParam("gender", "male")
            .get("/employees");

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        Assert.assertEquals(5, response.jsonPath().getList("$").size());

        Assert.assertEquals(0, response.jsonPath().getLong("[0].id"));
        Assert.assertEquals("Xiaoming", response.jsonPath().get("[0].name"));
        Assert.assertEquals(20, response.jsonPath().getLong("[0].age"));
        Assert.assertEquals("Male", response.jsonPath().get("[0].gender"));
        Assert.assertEquals(0, response.jsonPath().getLong("[0].salary"));
    }

    @Test
    public void should_return_employee_1_when_get_1() {
        MockMvcResponse response = RestAssuredMockMvc.given().contentType(ContentType.JSON)
            .get("/employees/1");

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        Assert.assertEquals(1, response.jsonPath().getLong("id"));
        Assert.assertEquals("Xiaohong", response.jsonPath().get("name"));
        Assert.assertEquals(19, response.jsonPath().getLong("age"));
        Assert.assertEquals("Male", response.jsonPath().get("gender"));
        Assert.assertEquals(0, response.jsonPath().getLong("salary"));
    }

    @Test
    public void should_return_correct_employee_when_create() {
        MockMvcResponse response = RestAssuredMockMvc.given().contentType(ContentType.JSON)
            .body("{" +
                "\"id\": 10," +
                "\"name\": \"Test\"," +
                "\"age\": 19," +
                "\"gender\": \"Male\"," +
                "\"salary\": 0" +
                "}")
            .post("/employees");

        Assert.assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());

        Assert.assertEquals(10, response.jsonPath().getLong("id"));
        Assert.assertEquals("Test", response.jsonPath().get("name"));
        Assert.assertEquals(19, response.jsonPath().getLong("age"));
        Assert.assertEquals("Male", response.jsonPath().get("gender"));
        Assert.assertEquals(0, response.jsonPath().getLong("salary"));
    }

    @Test
    public void should_return_200_when_delete() {
        MockMvcResponse response = RestAssuredMockMvc.given().contentType(ContentType.JSON)
            .delete("/employees/1");

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void should_return_correct_employee_when_update() {
        MockMvcResponse response = RestAssuredMockMvc.given().contentType(ContentType.JSON)
            .body("{" +
                "\"name\": \"New name\"" +
                "}")
            .put("/employees/1");

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        Assert.assertEquals(1, response.jsonPath().getLong("id"));
        Assert.assertEquals("New name", response.jsonPath().get("name"));
        Assert.assertEquals(19, response.jsonPath().getLong("age"));
        Assert.assertEquals("Male", response.jsonPath().get("gender"));
        Assert.assertEquals(0, response.jsonPath().getLong("salary"));
    }
}
