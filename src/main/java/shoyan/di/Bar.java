package shoyan.di;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class Bar {
    @Inject
    private Foo foo;

    String getMessage() {
        return "Bar called " + foo.getMessage();
    }
}
