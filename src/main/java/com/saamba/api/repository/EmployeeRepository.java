package com.saamba.api.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saamba.api.config.clients.DiscoveryClient;
import com.saamba.api.config.clients.ToneClient;
import com.saamba.api.entity.employee.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service level logic to handle reporting back to main controller
 * and construct majority of API calls from front-end.
 */
@Slf4j
@Repository("employee")
public class EmployeeRepository {

    @Autowired
    private DynamoDBMapper mapper;

    @Resource(name="discovery")
    private DiscoveryClient discoveryClient;

    @Resource(name="tone")
    private ToneClient toneClient;

    /**
     * Pushes changes to an employee in the table. This is where the query logic to update results
     * will go.
     * @param employeeId- updated user object
     * @return          - confirmation of update
     */
    public String updateEmployee(String employeeId) {
        Employee employee = findEmployeeById(employeeId);
        mapper.save(employee);
        return "record updated ...";
    }

    /**
     * Add a new user to the user table.
     * @param employeeId- JPA entity of user
     * @param fname     - employee's first name
     * @param lname     - employee's last name
     * @return          - returns acknowledgement
     */
    public String addUser(String employeeId, String fname, String lname) {
        mapper.save(Employee.builder().employeeId(employeeId)
                .firstName(fname)
                .lastName(lname)
                .build());
        return employeeId;
    }

    /**
     * Queries users in table through primary key, twitter handle.
     * @param employeeId- string twitter handle
     * @return          - referenced user object representation
     */
    public Employee findEmployeeById(String employeeId) {
        return mapper.load(Employee.class, employeeId);
    }

    /**
     * Deletes user from table.
     * @param employeeId- user object reference to delete
     * @return          - returns confirmation of delete
     */
    public String deleteUser(String employeeId) {
        mapper.delete(findEmployeeById(employeeId));
        return employeeId + " removed !!";
    }

    /**
     * Creates the DynamoDB save expression for updates to the user
     * object using DDB mapper.
     * @param user      - user object to update
     * @return          - returns the expression to execute to caller
     */
    private DynamoDBSaveExpression buildExpression(Employee user) {
        DynamoDBSaveExpression dynamoDBSaveExpression = new DynamoDBSaveExpression();
        Map<String, ExpectedAttributeValue> expectedMap = new HashMap<>();
        expectedMap.put("employeeId", new ExpectedAttributeValue(new AttributeValue().withS(user.getEmployeeId())));
        dynamoDBSaveExpression.setExpected(expectedMap);
        return dynamoDBSaveExpression;
    }
}
