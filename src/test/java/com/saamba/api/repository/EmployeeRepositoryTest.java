package com.saamba.api.repository;

import com.saamba.api.config.clients.DiscoveryClient;
import com.saamba.api.config.clients.ToneClient;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class EmployeeRepositoryTest {

    @Autowired
    private ToneClient toneClient;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Test
    void toneClientLoads() throws Exception {
        assertThat(toneClient).isNotNull();
    }

    @Test
    void discoveryClientLoads() throws Exception {
        assertThat(discoveryClient).isNotNull();
    }

}
