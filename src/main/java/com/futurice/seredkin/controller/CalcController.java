package com.futurice.seredkin.controller;

import com.futurice.seredkin.api.CalculusResult;
import com.futurice.seredkin.service.CalcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class CalcController {

    @Autowired CalcService calcService;

    @RequestMapping(value = "/calculus", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<CalculusResult> main(@RequestParam String query,
                                               @RequestParam(required = false, defaultValue = "14") Integer scale) {
        return new ResponseEntity<>(
                new CalculusResult(calcService.evaluateExpression(new String(Base64Utils.decodeFromString(query)), scale, BigDecimal.ROUND_UP)),
                HttpStatus.OK);
    }

}
