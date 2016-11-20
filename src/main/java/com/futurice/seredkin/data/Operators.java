package com.futurice.seredkin.data;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Anton on 20.11.2016.
 */
public class Operators {

    public static final Map<String, Operator<BigDecimal>> ALL;

    static {
        List<Operator<BigDecimal>> list = new LinkedList<>();
        list.add(new Operator<>(1, "+", BigDecimal::add));
        list.add(new Operator<>(1, "-", BigDecimal::subtract));
        list.add(new Operator<>(2, "*", BigDecimal::multiply));
        //without rounding mode: http://stackoverflow.com/a/4591216/4799579
        list.add(new Operator<>(3, "/", (bigDecimal, bigDecimal2) -> bigDecimal.divide(bigDecimal2, MathContext.DECIMAL128)));
        list.add(new Operator<>(3, "%", BigDecimal::remainder));
        list.add(new Operator<>(4, "^", (x, y) -> x.pow(y.intValue())));
        ALL = Collections.unmodifiableMap(list.stream().collect(Collectors.toMap(Operator::getSign, (a) -> a)));
    }
}
