package com.futurice.seredkin.api;

import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.LinkedList;

import static com.futurice.seredkin.api.Operator.OPS;

public class ShuntingYard {

    String formatExpression(String ex) {
        ex = ex.replaceAll("(\\-?\\d*\\.?\\d+)", " $1 ").replaceAll("([\\+\\*/^])", " $1 ");
        ex = ex.replaceAll("([\\(\\)])", " $1 ");
        ex = ex.replaceAll("\\s+", " ");
        //ex = ex.replaceAll("\\(\\s", "(").replaceAll("\\s\\)", ")");
        ex = ex.replaceAll("^\\s+|\\s+$", "");//equal to ex.trim()
        return ex;
    }

    public BigDecimal infixToPostfix(String infixExpr) {
        return calcStack(toPostfix(formatExpression(infixExpr)));
    }

    private boolean isUpper(String op, String sub) {
        return OPS.containsKey(sub) && OPS.get(sub).compareTo(OPS.get(op))>=0;
    }

    String toPostfix(String infix) {
        StringBuilder out = new StringBuilder();
        Deque<String> stack = new LinkedList<>();

        for (String token : infix.split("\\s")) {
            // operator
            if (OPS.containsKey(token)) {
                while (!stack.isEmpty() && isUpper(token, stack.peek()))
                    out.append(stack.pop()).append(' ');
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.peek().equals("("))
                    out.append(stack.pop()).append(' ');
                stack.pop();
            } else {
                out.append(token).append(' ');
            }
        }

        while (!stack.isEmpty())
            out.append(stack.pop()).append(' ');

        return out.toString().trim();
    }

    BigDecimal calcStack(String postfix) {
        Deque<BigDecimal> stack = new LinkedList<>();
        for (String s : postfix.split("\\s")) {
            final com.futurice.seredkin.api.Operator<BigDecimal> operator = OPS.get(s);
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

}