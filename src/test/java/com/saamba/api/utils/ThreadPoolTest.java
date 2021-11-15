package com.saamba.api.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "/application-test.properties")
public class ThreadPoolTest {

    @Value("${test.utils.thread.max}")
    private int threadMax;

    @Value("${test.utils.task.max}")
    private int taskMax;

    @Test
    public void testThreadPool() throws Exception {
        ThreadPool pool = new ThreadPool(taskMax, threadMax);
        int[] expected = {2, 4, 6, 8, 10, 20};
        int[] base = {1, 2, 3, 4, 5, 10};
        for(int i = 0; i < base.length; ++i) {
            int j = i;
            pool.execute(() -> {
                assertThat(expected[j] == 2 * base[j]).isTrue();
            });
        }
        assertThat(pool.waitForCompletion()).isTrue();
    }

    @Test
    public void testStop() {
        ThreadPool pool = new ThreadPool(taskMax, threadMax);
        int runs = 10000;
        List<Integer> output = new ArrayList<>();
        for(int i = 0; i < runs; ++i) {
            int j = i;
            try {
                pool.execute(() -> {
                    output.add(j);
                });
            } catch(Exception e) { }
            if(i == 5000)
                pool.stop();
        }
        assertThat(pool.waitForCompletion()).isTrue();
        assertThat(output.size() < runs).isTrue();
    }
}
