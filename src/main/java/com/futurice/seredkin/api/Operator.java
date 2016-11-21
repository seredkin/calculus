package com.futurice.seredkin.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(of = "sign")
@AllArgsConstructor
public class Operator<T extends Number> implements Comparable<Operator<T>> {

    public static final Map<String, Operator<BigDecimal>> OPS;

    static {
        List<Operator<BigDecimal>> list = new LinkedList<>();
        list.add(new Operator<>(1, "+", BigDecimal::add));
        list.add(new Operator<>(2, "-", BigDecimal::subtract));
        list.add(new Operator<>(3, "*", BigDecimal::multiply));
        //without rounding mode: http://stackoverflow.com/a/4591216/4799579
        list.add(new Operator<>(4, "/", (bigDecimal, bigDecimal2) -> bigDecimal.divide(bigDecimal2, MathContext.DECIMAL128)));
        //list.add(new Operator<>(4, "%", BigDecimal::remainder));
        //list.add(new Operator<>(5, "^", (x, y) -> x.pow(y.intValue())));
        OPS = list.stream().collect(Collectors.toMap(Operator::getSign, (a) -> a));
    }

    Integer priority;
    String sign;
    BiFunction<T, T, T> function;

    @Override
    public int compareTo(Operator<T> o) {
        return priority.compareTo(o.getPriority());
    }

    @Override
    public String toString() {
        return sign;
    }
}
