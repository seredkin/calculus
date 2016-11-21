package com.futurice.seredkin.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Data @EqualsAndHashCode(of = "sign")
public class Operand<T extends Number> implements Comparable<Operand<T>> {

    public static final Map<String, Operand<BigDecimal>> operands;

    static {
        List<Operand<BigDecimal>> list = new LinkedList<>();
        list.add(new Operand<>(1, "+", BigDecimal::add));
        list.add(new Operand<>(2, "-", BigDecimal::subtract));
        list.add(new Operand<>(3, "*", BigDecimal::multiply));
        //without rounding mode: http://stackoverflow.com/a/4591216/4799579
        list.add(new Operand<>(4, "/", (bigDecimal, bigDecimal2) -> bigDecimal.divide(bigDecimal2, MathContext.DECIMAL128)));
        //possible extensions
        //list.add(new Operator<>(4, "%", BigDecimal::remainder));
        //list.add(new Operator<>(5, "^", (x, y) -> x.pow(y.intValue())));
        operands = list.stream().collect(Collectors.toMap(Operand::getSign, (a) -> a));
    }

    final Integer priority;
    final String sign;
    final BiFunction<T, T, T> function;

    @Override
    public int compareTo(Operand<T> o) {
        return priority.compareTo(o.getPriority());
    }

}
