package com.futurice.seredkin.service;

import com.futurice.seredkin.data.CalculusException;
import com.futurice.seredkin.data.Operators;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class ShuntingYard {

    private static Map<String, Operator> ops = new HashMap<String, Operator>() {{
        put("+", Operator.ADD);
        put("-", Operator.SUBTRACT);
        put("*", Operator.MULTIPLY);
        put("/", Operator.DIVIDE);
    }};

    private static boolean isHigherPrec(String op, String sub) {
        return (ops.containsKey(sub) && ops.get(sub).precedence >= ops.get(op).precedence);
    }

    public static String toPostfix(String infix) {
        StringBuilder output = new StringBuilder();
        Deque<String> stack = new LinkedList<>();

        for (String token : infix.split("\\s")) {
            // operator
            if (ops.containsKey(token)) {
                while (!stack.isEmpty() && isHigherPrec(token, stack.peek()))
                    output.append(stack.pop()).append(' ');
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.peek().equals("("))
                    output.append(stack.pop()).append(' ');
                stack.pop();
            } else {
                output.append(token).append(' ');
            }
        }

        while (!stack.isEmpty())
            output.append(stack.pop()).append(' ');

        return output.toString().trim();
    }

    public static BigDecimal calcStack(String postfix) {
        Deque<BigDecimal> stack = new LinkedList<>();
        for (String s : postfix.split("\\s")) {
            final com.futurice.seredkin.data.Operator<BigDecimal> operator = Operators.ALL.get(s);
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
        return stack.pop();
    }

    private enum Operator {
        ADD(1), SUBTRACT(2), MULTIPLY(3), DIVIDE(4);
        final int precedence;

        Operator(int p) {
            precedence = p;
        }
    }

}