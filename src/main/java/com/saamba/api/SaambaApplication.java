package com.saamba.api;

import com.saamba.api.entity.user.User;
import com.saamba.api.repository.MusicRepository;
import com.saamba.api.repository.UserRepository;
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
    private UserRepository entityRepo;

    @Resource(name = "music")
    private MusicRepository musicRepo;

    @GetMapping("/updateMusic")
    public String updateMusic() {
        log.info("Starting API call /updateMusic.");
        return musicRepo.updateMusic();
    }

    @PutMapping("/updateLyrics")
    public String updateLyrics() {
        log.info("Updating lyrics for all songs in music table without lyrics.");
        return musicRepo.updateLyrics();
    }

    @PutMapping("/backfillJson")
    public String backfillJson() {
        log.info("Request to backfill songs to json received.");
        return musicRepo.backfillGenres();
    }

    @PutMapping("/pullGenre/{genre}")
    public String pullGenre(@PathVariable String genre) {
        log.info("Conducting singular backfill for " + genre + ".");
        return musicRepo.pullGenre(genre);
    }

    @PutMapping("/updateGenre/{genre}")
    public String updateGenre(@PathVariable String genre) {
        log.info("Searching for newest music from Spotify for " + genre + ".");
        return musicRepo.updateGenre(genre);
    }

    /**
     * API end point which returns JSON string to caller via get request
     * from a user's Twitter handle.
     * @param accountName   - string
     * @return              - JSON string of spotify URI
     */
    @GetMapping("/getPlaylist/{accountName}")
    public String[] getPlaylist(@PathVariable String accountName) {
        log.info("Starting API call /getPlaylist/" + accountName);
        return entityRepo.getPlaylist(accountName);
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

    /**
     * Entry point for application.
     * @param args      - default args
     */
    public static void main(String[] args) {
        log.info("Initializing SaambaApplication.");
        SpringApplication.run(SaambaApplication.class, args);
    }
}
