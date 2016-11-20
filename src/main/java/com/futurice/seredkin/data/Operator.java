package com.futurice.seredkin.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.function.BiFunction;

/**
 * Created by Anton on 20.11.2016.
 */
@Data
@AllArgsConstructor public class Operator<T extends Number> implements Comparable<Operator<? extends Number>> {

    Integer priority;
    String sign;
    BiFunction<T, T, T> function;

    @Override public int compareTo(Operator<? extends Number> o) {
        return priority.compareTo(o.getPriority());
    }

    @Override public String toString() {
        return sign;
    }
}
