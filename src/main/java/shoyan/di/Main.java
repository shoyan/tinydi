package shoyan.di;

public class Main {
    public static void main(String[] args) {
        Context.register("foo", Foo.class);
        Context.register("bar", Bar.class);

        Bar bar = (Bar) Context.getBean("bar");

        System.out.println(bar.getMessage());
    }
}
