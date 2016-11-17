package com.futurice.seredkin.service;

import com.futurice.seredkin.data.CalculusException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Service("calcSpring")
public class CalcServiceSpringImpl implements CalcService {

    @Override public BigDecimal evaluateExpression(@NotNull String ex) {
        ExpressionParser parser = new SpelExpressionParser();
        try {
            return parser.parseExpression(ex).getValue(BigDecimal.class);
        } catch (SpelParseException e){
            throw new CalculusException(e.getMessage());
        }
    }
}
