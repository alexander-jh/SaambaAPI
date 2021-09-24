package com.saamba.api;

import com.saamba.api.entity.user.User;
import com.saamba.api.repository.MusicRepository;
import com.saamba.api.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@SpringBootApplication
@RestController
public class SaambaApplication {

    @Resource(name = "user")
    private UserRepository entityRepo;

    @Resource(name = "music")
    private MusicRepository musicRepo;

    @GetMapping("/updateMusic")
    public String updateMusic() { return musicRepo.updateMusic(); }

    @PostMapping("/saveUser")
    public User saveUser(@RequestBody User user) {
        return entityRepo.addUser(user);
    }

    @GetMapping("/getUser/{accountName}")
    public User findUser(@PathVariable String accountName) {
        return entityRepo.findUserByAccount(accountName);
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestBody User user) {
        return entityRepo.deleteUser(user);
    }

    @PutMapping("/editUser")
    public String updateUser(@RequestBody User user) {
        return entityRepo.editUser(user);
    }

    public static void main(String[] args) {
        SpringApplication.run(SaambaApplication.class, args);
    }

}
