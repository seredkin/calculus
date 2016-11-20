package com.futurice.seredkin.service;

import com.futurice.seredkin.data.CalculusException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service(CalcService.CALC_SPEL)
public class CalcServiceSpelImpl implements CalcService {

    @Override public BigDecimal evaluateExpression(String ex) {
        ExpressionParser parser = new SpelExpressionParser();
        try {
            return parser.parseExpression(ex).getValue(BigDecimal.class);
        } catch (SpelParseException e){
            throw new CalculusException(e.getMessage());
        }
    }
}
