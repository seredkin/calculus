package com.futurice.seredkin;

import lombok.extern.log4j.Log4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StopWatch;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

import static junit.framework.TestCase.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class) @Log4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SimplePerformanceTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void performanceTest() throws Exception {
        int threads = 20;
        int samples = 100;
        int desiredFPS = 50;
        long timeOutSec = (long)samples*threads/desiredFPS;
        final LongAdder counter = new LongAdder();
        CompletableFuture[] parallel = new CompletableFuture[threads];
        for (int i = 0; i < threads; i++) {
            CompletableFuture<Integer> sequential = CompletableFuture.supplyAsync(() -> execFunction(counter));
            for (int j = 0; j < samples; j++) {
                    sequential.thenCombine(sequential, (integer, integet1) -> execFunction(counter));
            }
            parallel[i] = sequential;
        }
        StopWatch sw = new StopWatch();
        sw.start();
        CompletableFuture.allOf(parallel)
                .exceptionally(throwable -> {log.error(throwable); return null;})
                .get(timeOutSec, TimeUnit.SECONDS);
        sw.stop();
        log.info("Performance result: "+counter.intValue()+" samples in "+sw.getLastTaskTimeMillis()+"ms");
    }

    private Integer execFunction(LongAdder counter) {
        counter.add(1);
        int i = counter.intValue()%4+1;
        try {
            String body = mockMvc.perform(get("//calculus?query=" + i).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
            assertTrue(body.contains("result"));
        } catch (Exception e) {
            log.error(e);
        }
        return counter.intValue();
    }

}
