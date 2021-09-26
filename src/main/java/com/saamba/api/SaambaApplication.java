package com.saamba.api;

import com.saamba.api.entity.user.User;
import com.saamba.api.repository.MusicRepository;
import com.saamba.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@SpringBootApplication
@RestController
@Slf4j
public class SaambaApplication {

    @Resource(name = "user")
    private UserRepository entityRepo;

    @GetMapping("/getPlaylist/{accountName}")
    public String getPlaylist(@PathVariable String accountName) {
        log.info("Starting API call /getPlaylist/" + accountName);
        return entityRepo.getPlaylist(accountName);
    }

/*
    Functionality degraded for the sake of first milestone.

    @Resource(name = "music")
    private MusicRepository musicRepo;

    @GetMapping("/updateMusic")
    public String updateMusic() {
        log.info("Starting API call /updateMusic.");
        return musicRepo.updateMusic();
    }

    @PostMapping("/saveUser")
    public User saveUser(@RequestBody User user) {
        log.info("Starting API call /saveUser.");
        return entityRepo.addUser(user);
    }

    @GetMapping("/getUser/{accountName}")
    public User findUser(@PathVariable String accountName) {
        log.info("Starting API call /getUser/" + accountName);
        return entityRepo.findUserByAccount(accountName);
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestBody User user) {
        log.info("Starting API call /deleteUser.");
        return entityRepo.deleteUser(user);
    }

    @PutMapping("/editUser")
    public String updateUser(@RequestBody User user) {
        log.info("Starting API call /editUser.");
        return entityRepo.editUser(user);
    }

*/
    public static void main(String[] args) {
        log.info("Initializing SaambaApplication.");
        SpringApplication.run(SaambaApplication.class, args);
    }
}
