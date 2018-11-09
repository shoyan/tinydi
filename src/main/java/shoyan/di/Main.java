package shoyan.di;

public class Main {
    public static void main(String[] args) {
        Context.register("foo", Foo.class);

        Foo foo = (Foo) Context.getBean("foo");

        System.out.println(foo.getMessage());
    }
}
