package com.saamba.api;

import com.saamba.api.entity.Person;
import com.saamba.api.repository.MusicRepository;
import com.saamba.api.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class SaambaApplication {

    @Autowired
    private PersonRepository entityRepo;

    @Autowired
    private MusicRepository musicRepo;

    @PostMapping("/updateMusic")
    public String updateMusic() { return musicRepo.updateMusic(); }

    @PostMapping("/savePerson")
    public Person savePerson(@RequestBody Person person) {
        return entityRepo.addPerson(person);
    }

    @GetMapping("/getPerson/{personId}")
    public Person findPerson(@PathVariable String personId) {
        return entityRepo.findPersonByPersonId(personId);
    }

    @DeleteMapping("/deletePerson")
    public String deletePerson(@RequestBody Person person) {
        return entityRepo.deletePerson(person);
    }

    @PutMapping("/editPerson")
    public String updatePerson(@RequestBody Person person) {
        return entityRepo.editPerson(person);
    }

    public static void main(String[] args) {
        SpringApplication.run(SaambaApplication.class, args);
    }

}
