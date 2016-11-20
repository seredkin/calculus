package com.futurice.seredkin;

import com.futurice.seredkin.data.CalculusException;
import com.futurice.seredkin.data.Operator;
import com.futurice.seredkin.service.CalcServiceShuntYardImpl;
import com.futurice.seredkin.service.ShuntingYard;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class CalculusTest {

    private Map<String, Operator<BigDecimal>> operatorMap;

    @Before
    public void init() {

        List<Operator<BigDecimal>> list = new LinkedList<>();
        list.add(new Operator<>(1, "+", BigDecimal::add));
        list.add(new Operator<>(1, "-", BigDecimal::subtract));
        list.add(new Operator<>(2, "*", BigDecimal::multiply));
        //without rounding mode: http://stackoverflow.com/a/4591216/4799579
        list.add(new Operator<>(3, "/", (bigDecimal, bigDecimal2) -> bigDecimal.divide(bigDecimal2, MathContext.DECIMAL128)));
        list.add(new Operator<>(3, "%", BigDecimal::remainder));
        list.add(new Operator<>(4, "^", (x, y) -> x.pow(y.intValue())));
        operatorMap = list.stream().collect(Collectors.toMap(Operator::getSign, (a) -> a));
    }

    @Test
    public void testPostfix() {
        Queue<Pair<String, String>> testData = new ArrayDeque<>(50);
        testData.add(new ImmutablePair<>("2 * (23/(3*3))- 23 * (2*3)", "2 23 3 3 * / * 23 2 3 * * -"));
        testData.add(new ImmutablePair<>("52+(1+2)*4 - -3", "52 1 2 + 4 * -3 - +"));//minus negative
        testData.add(new ImmutablePair<>("52+((1+2)*4)- 3", "52 1 2 + 4 * 3 - +"));
        testData.add(new ImmutablePair<>("9+24/(7 - 3)", "9 24 7 3 - / +"));
        testData.add(new ImmutablePair<>("1", "1")); // positive number
        testData.add(new ImmutablePair<>("-1", "-1")); // negative number
        testData.add(new ImmutablePair<>("1+1", "1 1 +")); // simple addition
        testData.add(new ImmutablePair<>(" 1 + 1 ", "1 1 +")); // simple addition with whitechars
        testData.add(new ImmutablePair<>(" 1 + -1 ", "1 -1 +")); // simple addition with neg. number & whitechars
        testData.add(new ImmutablePair<>("(1+1)", "1 1 +")); // simple addition with brackets

        testData.forEach(p -> {
            final String left = CalcServiceShuntYardImpl.formatExpression(p.getLeft());
            final String postfix = ShuntingYard.toPostfix(left);
            System.out.println("ex =\t" + left + "\t" + postfix);
            assertEquals(p.getRight(), postfix);
            System.out.println("result\t" + calcStack(postfix));
        });

    }

    private Double calcStack(String postfix) {
        Deque<BigDecimal> stack = new LinkedList<>();
        for (String s : postfix.split("\\s")) {
            final Operator<BigDecimal> operator = operatorMap.get(s);
            if (operator == null) {
                if (NumberUtils.isNumber(s))
                    stack.push(new BigDecimal(NumberUtils.toDouble(s)));
                else
                    throw new CalculusException("Unknown literal: " + s);
            } else if (operator.getFunction() != null) {
                final BigDecimal right = stack.pop();
                final BigDecimal left = stack.pop();
                stack.push((operator.getFunction().apply(left, right)));
            } else
                throw new CalculusException("Operator without a defined function: " + s);
        }
        return stack.pop().doubleValue();
    }

}
