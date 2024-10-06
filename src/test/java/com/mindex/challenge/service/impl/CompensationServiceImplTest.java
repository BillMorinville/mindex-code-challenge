package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationGetUrl;
    private String compensationCreateUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationGetUrl = "http://localhost:" + port + "/compensation/get/{id}";
        compensationCreateUrl = "http://localhost:" + port + "/compensation/create/{id}";
    }

    @Test
    public void testCreateReadCompensation() {
        //creating test compensation
        Compensation testCompensation = new Compensation(UUID.randomUUID().toString(), "100,000", "10-06-2024");

        // testing POST REST endpoint
        Compensation createdCompensation = restTemplate.postForEntity(compensationCreateUrl, testCompensation, Compensation.class, testCompensation.getEmployeeId()).getBody();

        //verifying employeeId is not null
        assertNotNull(createdCompensation.getEmployeeId());

        //verifying compensation values match
        assertCompensation(testCompensation, createdCompensation);

        // testing GET REST endpoint
        Compensation readCompensation = restTemplate.getForEntity(compensationGetUrl, Compensation.class, createdCompensation.getEmployeeId()).getBody();

        //verifying employeeId is not null
        assertEquals(createdCompensation.getEmployeeId(), readCompensation.getEmployeeId());

        //verifying compensation values match
        assertCompensation(createdCompensation, readCompensation);
    }

    private static void assertCompensation(Compensation expected, Compensation actual) {
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }

}
