package com.saamba.api;

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

    @Resource(name = "employee")
    private EmployeeRepository employeeRepo;

    @PostMapping("/addEmployee/{employeeId}/{fname}/{lname}")
    public String addEmployee(@PathVariable String employeeId, @PathVariable String fname,
                              @PathVariable String lname) {
        log.info("Starting API call /addEmployee" + employeeId + " " + fname + " " + lname + ".");
        return employeeRepo.addUser(employeeId, fname, lname);
    }

    @GetMapping("/findEmployee/{employeeId}")
    public Employee findEmployee(@PathVariable String employeeId) {
        log.info("Starting API call /findEmployee/" + employeeId);
        return employeeRepo.findEmployeeById(employeeId);
    }

    @DeleteMapping("/deleteEmployee/{employeeId}")
    public String deleteEmployee(@PathVariable String employeeId) {
        log.info("Starting API call /deleteEmployee.");
        return employeeRepo.deleteUser(employeeId);
    }

//    @PutMapping("/editEmployee")
//    public String updateUser(@RequestBody Employee employee) {
//        log.info("Starting API call /editEmployee" + employee + ".");
//        return employeeRepo.editUser(employee);
//    }

    /**
     * Entry point for application.
     * @param args      - default args
     */
    public static void main(String[] args) {
        log.info("Initializing SaambaApplication.");
        SpringApplication.run(SaambaApplication.class, args);
    }
}
