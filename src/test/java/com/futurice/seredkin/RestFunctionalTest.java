package com.futurice.seredkin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.futurice.seredkin.data.CalculusResult;
import com.futurice.seredkin.service.CalcService;
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
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*An example calculus query:

    Original query: 2 * (23/(3*3))- 23 * (2*3)
    With encoding: MiAqICgyMy8oMyozKSktIDIzICogKDIqMyk=

== API Description ==

    Endpoint:
        GET /calculus?query=[input]
        The input can be expected to be UTF-8 with BASE64 encoding
        Return:
        On success: JSON response of format: { error: false, result: number }
        On error: Either a HTTP error code or: { error: true, message: string }

    Supported operations: + - * / ( )*/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class RestFunctionalTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        this.mapper = new ObjectMapper();
    }

    @Test
    public void originalExpr() throws Exception {
        String ex = "2 * (23.0/(3*3))- 23 * (2*3)";
        String body = this.mockMvc.perform(get("/calculus?query=" + Base64Utils.encodeToUrlSafeString(ex.getBytes())).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        final CalculusResult result = mapper.readerFor(CalculusResult.class).readValue(body);
        final BigDecimal scaledResult = result.getResult().setScale(14, BigDecimal.ROUND_UP);
        final BigDecimal expectedResult = new BigDecimal(-132.88888888888889).setScale(14, BigDecimal.ROUND_UP);
        assertEquals(expectedResult, scaledResult);//
    }

    @Test
    public void faultyExpr() throws Exception {
        String ex = "2 * (23/(3*3))- 23 * (2*3";
        String body = this.mockMvc.perform(get("/calculus?query=" + Base64Utils.encodeToUrlSafeString(ex.getBytes())).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        final CalculusResult result = mapper.readerFor(CalculusResult.class).readValue(body);
        assertNotNull(result.getMessage());
    }

    @Test
    public void nullExpr() {
        final CalcService calcService = wac.getBean(CalcService.class);
        assertNull(calcService.evaluateExpression(null));
    }

}
