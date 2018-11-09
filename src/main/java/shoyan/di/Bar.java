package shoyan.di;

import javax.inject.Inject;

public class Bar {
    @Inject
    private Foo foo;

    String getMessage() {
        return "Bar called " + foo.getMessage();
    }
}
