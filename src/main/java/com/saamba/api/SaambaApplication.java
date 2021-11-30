package com.saamba.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.saamba.api.entity.employee.Employee;
import com.saamba.api.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * I will consistently try to pool a working task list into this
 * centralized class.
 * TODO : Tie JUnit testing into GitHub workflow
 * TODO : Test cases for discovery client
 * TODO : Test cases for tone analyzer client
 * TODO : Test cases for twitter config
 * TODO : Test cases for user repo
 */
@SpringBootApplication
@RestController
@Slf4j
public class SaambaApplication {

    @Resource(name = "user")
    private EmployeeRepository entityRepo;

    /**
     * API end point which returns JSON string to caller via get request
     * from a user's Twitter handle.
     * @param employeeId   - string
     * @return              - JSON string of spotify URI
     */
    @GetMapping("/getPlaylist/{accountName}")
    public String getEvaluation(@PathVariable String employeeId) throws JsonProcessingException {
        log.info("Starting API call /getPlaylist/" + employeeId);
        return entityRepo.getPlaylist(employeeId);
    }

    @PostMapping("/saveUser")
    public Employee saveUser(@RequestBody Employee user) {
        log.info("Starting API call /saveUser.");
        return entityRepo.addUser(user);
    }

    @GetMapping("/getUser/{accountName}")
    public Employee findUser(@PathVariable String accountName) {
        log.info("Starting API call /getUser/" + accountName);
        return entityRepo.findUserByAccount(accountName);
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestBody Employee user) {
        log.info("Starting API call /deleteUser.");
        return entityRepo.deleteUser(user);
    }

    @PutMapping("/editUser")
    public String updateUser(@RequestBody Employee user) {
        log.info("Starting API call /editUser.");
        return entityRepo.editUser(user);
    }

    /**
     * Entry point for application.
     * @param args      - default args
     */
    public static void main(String[] args) {
        log.info("Initializing SaambaApplication.");
        SpringApplication.run(SaambaApplication.class, args);
    }
}
