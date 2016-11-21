package com.futurice.seredkin;

import com.futurice.seredkin.api.ShuntingYardTest;
import junit.framework.AssertionFailedError;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.util.StopWatch;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
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

    private List<Pair<String, String>> testData;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void performanceTest() throws Exception {
        int threads = 20;
        int samples = 10000;
        int desiredFPS = 5000;//minimal samples per second per thread
        long timeOutmSec = (long)samples*threads/desiredFPS*1000;
        final LongAdder counter = new LongAdder();
        final StopWatch sw = new StopWatch();
        sw.start();
        CompletableFuture[] parallel = new CompletableFuture[threads];
        testData = new ArrayList<>(ShuntingYardTest.genTestData());
        for (int i = 0; i < threads; i++) {
            CompletableFuture<Integer> sequential = CompletableFuture.supplyAsync(() -> execFunction(counter));
            for (int j = 0; j < samples; j++) {
                    sequential.thenCombine(sequential, (int0, int1) -> execFunction(counter));
            }
            parallel[i] = sequential;
        }
        CompletableFuture.allOf(parallel)
                .exceptionally(throwable -> {log.error(throwable); return null;})
                .get(timeOutmSec, TimeUnit.SECONDS);
        sw.stop();
        log.info("Performance result: "+counter.intValue()+" samples in "+sw.getTotalTimeMillis()+"ms");
        if(sw.getLastTaskTimeMillis()>timeOutmSec)
            throw new AssertionFailedError("Performance test timed out");
    }

    private Integer execFunction(LongAdder counter) {
        counter.add(1);
        int i = counter.intValue()%4+1;
        String infix = testData.get(i % testData.size()).getLeft();
        String base64 = Base64Utils.encodeToUrlSafeString(infix.getBytes());
        try {
            String body = mockMvc.perform(get("/calculus?query=" + base64).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
            assertTrue(body.contains("result"));
        } catch (Exception e) {
            log.error(e);
        }
        return counter.intValue();
    }

}
