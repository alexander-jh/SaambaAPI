package com.saamba.api;

import com.saamba.api.repository.MusicRepository;
import com.saamba.api.repository.EmployeeRepository;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class SaambaApplicationTest {

	@Autowired
	private EmployeeRepository userRepo;

	@Autowired
	private MusicRepository musicRepo;

	@Test
	void userRepoLoads() throws Exception {
		assertThat(userRepo).isNotNull();
	}

	@Test
	void musicRepoLoads() throws Exception {
		assertThat(musicRepo).isNotNull();
	}

}
