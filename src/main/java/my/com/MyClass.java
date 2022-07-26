package my.com;

import junit.framework.TestCase;
import org.mvel.MVEL;
import org.mvel.compiler.AbstractParser;

import java.util.HashMap;

public class MyClass extends TestCase {
    boolean run;

    public MyClass() {
        super();
        run = AbstractParser.OPERATORS.containsKey("proto");
    }

    public void testProtoFieldAccess() {
        if (!run) return;

        Object o =
                MVEL.eval(
                        "proto Person { int age = 5; String name; }; (p = new Person()).age",
                        new HashMap<String, Object>());
        assertEquals(5, o);
    }

    public void testProtoWithFunction() {
        if (!run) return;

        Object o =
                MVEL.eval(
                        "proto Person { "
                                + "               int age = 2; "
                                + "               def multAge() { "
                                + "                   age * 10 "
                                + "               }; "
                                + "             };"
                                + "             p = new Person(); "
                                + "             p.multAge();",
                        new HashMap<String, Object>());
        assertEquals(20, o);
    }

    public void testProtoWithFunction2() {
        if (!run) return;

        String ex =
                "proto Adder {"
                        + "int count = 0;"
                        + "def accumulate() {"
                        + "if (count < 10) {"
                        + "System.out.println('counting:' + count);"
                        + "count++;"
                        + "accumulate();"
                        + "}"
                        + "}"
                        + "};"
                        + "adder = new Adder();"
                        + "adder.accumulate();"
                        + "adder.count;";

        Object o = MVEL.eval(ex, new HashMap<String, Object>());
        assertEquals(10, o);
    }

    public void testProtoWithOtherProtoRef() {
        if (!run) return;

        String ex =
                "proto Parent { Child child = new Child(); }; proto Child { Parent parent; }; "
                        + "Parent parent = new Parent(); if (parent.child.parent == null) { 'YEP' } else { 'NOPE' }";
        Object o = MVEL.eval(ex, new HashMap<String, Object>());
        assertEquals("YEP", o);
    }
}