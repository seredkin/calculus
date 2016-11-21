package com.futurice.seredkin.api;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.LinkedList;
import java.util.regex.Pattern;

import static com.futurice.seredkin.api.Operand.operands;

public class ShuntingYard {

    final static Pattern digitPattern = Pattern.compile("(\\-?\\d*\\.?\\d+)");
    final static Pattern operandPattern = Pattern.compile("([\\+\\*/^])");
    final static Pattern bracketPattern = Pattern.compile("([\\(\\)])");
    final static Pattern spacePattern = Pattern.compile("\\s+");
    final static Pattern outerSpacePattern = Pattern.compile("^\\s+|\\s+$");


    String formatExpression(String ex) {
        ex = digitPattern.matcher(ex).replaceAll(" $1 ");
        ex = operandPattern.matcher(ex).replaceAll(" $1 ");
        ex = bracketPattern.matcher(ex).replaceAll(" $1 ");
        ex = spacePattern.matcher(ex).replaceAll(" ");
        ex = outerSpacePattern.matcher(ex).replaceAll("");
        return ex;
    }

    public BigDecimal infixToPostfix(String infixExpr) {
        return calcStack(toPostfix(formatExpression(infixExpr)));
    }

    private boolean isUpper(String op, String sub) {
        return operands.containsKey(sub) && operands.get(sub).compareTo(operands.get(op)) >= 0;
    }

    String toPostfix(String infix) {
        StringBuilder out = new StringBuilder();
        Deque<String> stack = new LinkedList<>();

        for (String token : infix.split("\\s")) {
            // operator
            if (operands.containsKey(token)) {
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
            final Operand<BigDecimal> operand = operands.get(s);
            if (operand == null) {
                if (digitPattern.matcher(s).matches())
                    stack.push(new BigDecimal(Double.valueOf(s)));
                else
                    throw new CalculusException("Unknown literal: " + s);
            } else {
                final BigDecimal right = stack.pop();
                final BigDecimal left = stack.pop();
                stack.push((operand.getFunction().apply(left, right)));
            }
        }
        return stack.pop();
    }

}