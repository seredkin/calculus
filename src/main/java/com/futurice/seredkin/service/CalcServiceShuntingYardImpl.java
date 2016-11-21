package com.futurice.seredkin.service;

import com.futurice.seredkin.api.ShuntingYard;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
class CalcServiceShuntingYardImpl implements CalcService {

    private final ShuntingYard shuntingYard = new ShuntingYard();

    @Override public BigDecimal evaluateExpression(String infix) {
        return shuntingYard.infixToPostfix(infix);
    }

}
