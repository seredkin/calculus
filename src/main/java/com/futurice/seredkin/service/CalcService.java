package com.futurice.seredkin.service;

import java.math.BigDecimal;

public interface CalcService {

    /** Spring's spel calculator */
    String CALC_SPEL = "calcSpel";
    /** Custom implementation*/
    String CALC_SHUNT_YARD = "calcShuntYard";

    BigDecimal evaluateExpression(String ex);
}
