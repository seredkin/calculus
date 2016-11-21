package com.futurice.seredkin.api;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayDeque;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class ShuntingYardTest {


    @Test
    public void testPostfix() {
        ShuntingYard shuntingYard = new ShuntingYard();
        Queue<Pair<String, String>> testData = genTestData();

        testData.forEach(p -> {
            final String infix = shuntingYard.formatExpression(p.getLeft());
            final String postfix = shuntingYard.toPostfix(infix);
            System.out.println("ex =\t" + infix + "\t" + postfix);
            assertEquals(p.getRight(), postfix);
            System.out.println("result\t" + shuntingYard.calcStack(postfix));
        });

    }

    public static Queue<Pair<String, String>> genTestData() {
        Queue<Pair<String, String>> testData;
        testData = new ArrayDeque<>(50);
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
        return testData;
    }

}
