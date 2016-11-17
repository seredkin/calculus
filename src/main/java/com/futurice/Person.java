package com.futurice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Java Bean Person
 *
 * @author Ivan Gomez
 */
@Data @NoArgsConstructor @ToString(of = "name") @EqualsAndHashCode(of = "name")
public class Person {

    private static final Person[] staticPerson = new Person[]{
            new Person("Name 1", "Spouse 1", false, 1),
            new Person("Name 2", "Spouse 2", true, 3),
            new Person("Name 3", "Spouse 3", true, 0)
    };
    private static final Person NULL_VALUE = new Person("Empty Name", "Empty spouse", false, 0);

    private String name = "";
    private boolean married;
    private String spouse = "";
    private List<String> children = Collections.emptyList();

    private Person(String name, String spouse, boolean isMarried, int nChildren) {
        super();
        this.name = name;
        this.spouse = spouse;
        this.married = isMarried;
        children = new ArrayList<>();
        for (int z = 0; z < nChildren; z++) {
            children.add("Child " + z);
        }
    }

    public static Person lookup(String id) {
        if (id != null && id.matches("\\d*")) {//preventing non-digits and negative integers
            int idInt = Integer.parseInt(id);
            return idInt <= staticPerson.length ? staticPerson[--idInt] : NULL_VALUE;
        }
        throw new IllegalArgumentException("Unknown id of a Person entity: "+id);
    }
}
