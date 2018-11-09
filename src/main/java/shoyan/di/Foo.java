package shoyan.di;

import javax.inject.Named;

@Named
public class Foo {
    public Foo() {
    }

    String getMessage() {
        return "Foo!";
    }
}
