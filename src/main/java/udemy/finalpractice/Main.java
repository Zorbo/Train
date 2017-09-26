package udemy.finalpractice;

public class Main {

    public static void main(String[] args) {

        SomeClass one = new SomeClass("one");
        SomeClass two = new SomeClass("two");
        SomeClass three = new SomeClass("three");

        System.out.println(one.getInstanceNumber());
        System.out.println(two.getInstanceNumber());
        System.out.println(three.getInstanceNumber());

      //  one.instanceNumber = 5;

        System.out.println(Math.PI);
       // Math m = new Math()

        int pw = 674523;
        Password password = new Password(pw);
        password.storePassword();

        password.letMeIn(1);
        password.letMeIn(5577755);
        password.letMeIn(9773);
        password.letMeIn(0);
        password.letMeIn(-1);
        password.letMeIn(674523);
    }
}
