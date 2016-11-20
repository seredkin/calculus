package com.futurice.seredkin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by Anton on 17.11.2016.
 */
@Service(CalcService.CALC_SHUNT_YARD) @Primary @Slf4j
public class CalcServiceShuntYardImpl implements CalcService {

    public static String formatExpression(String ex) {
        ex = ex.replaceAll("(\\-?\\d*\\.?\\d+)", " $1 ").replaceAll("([\\+\\*/^])", " $1 ");
        ex = ex.replaceAll("([\\(\\)])", " $1 ");
        ex = ex.replaceAll("\\s+", " ");
        //ex = ex.replaceAll("\\(\\s", "(").replaceAll("\\s\\)", ")");
        ex = ex.replaceAll("^\\s+|\\s+$", "");//equal to ex.trim()
        return ex;
    }

    @Override public BigDecimal evaluateExpression(String infix) {

        //string reformat
        infix = formatExpression(infix);
        infix = CalcServiceShuntYardImpl.formatExpression(infix); //= infixToPostfix(ex);
        final String postfix = ShuntingYard.toPostfix(infix);

        log.debug("Postfix string:\t" + infix);

        return ShuntingYard.calcStack(postfix);
    }

}
