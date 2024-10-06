package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String numberOfReports;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        numberOfReports = "http://localhost:" + port + "/employee/get-reports/{id}";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);


    }

    @Test
    public void testReportingStructure() {
        // Create each reporting employee (total of 3 for this scenario)
        List<Employee> reports = createEmployees();

        // Create lead employee
        Employee bossMan = new Employee();
        bossMan.setFirstName("Michael");
        bossMan.setLastName("Jordan");
        bossMan.setDepartment("Engineering");
        bossMan.setPosition("Lead Developer");
        bossMan.setDirectReports(reports);

        // Create expected response
        ReportingStructure bossManReportingStructure = new ReportingStructure(bossMan, 3);

        // Insert leadEmployee into db
        Employee createdBossMan = restTemplate.postForEntity(employeeUrl, bossMan, Employee.class).getBody();

        // verify that employeeId was created
        assertNotNull(createdBossMan.getEmployeeId());
        //verify employee attributes are equal
        assertEmployeeEquivalence(bossMan, createdBossMan);

        //making call to new numberOfReports endpoint
        ReportingStructure reportingStructure =
                restTemplate.getForEntity(numberOfReports, ReportingStructure.class, createdBossMan.getEmployeeId()).getBody();

        //verify response
        assertReportingStructure(bossManReportingStructure, reportingStructure);
    }

    private static List<Employee> createEmployees() {
        Employee report1 = new Employee();

        report1.setFirstName("Steve");
        report1.setLastName("Kerr");
        report1.setDepartment("Engineering");
        report1.setPosition("Developer");

        Employee report2 = new Employee();

        report2.setFirstName("Dennis");
        report2.setLastName("Rodman");
        report2.setDepartment("Engineering");
        report2.setPosition("Developer");

        Employee report3 = new Employee();

        report3.setFirstName("Scottie");
        report3.setLastName("Pippen");
        report3.setDepartment("Engineering");
        report3.setPosition("Developer");

        List<Employee> reports = new ArrayList<>();
        reports.add(report1);
        reports.add(report2);
        reports.add(report3);
        return reports;
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

    private static void assertReportingStructure(ReportingStructure expected, ReportingStructure actual) {
        assertEquals(expected.getNumberOfReports(), actual.getNumberOfReports());
    }
}
