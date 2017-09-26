package udemy.staticpractice;

public class Main {

    public static int multipler = 7;

    public static void main(String[] args) {

        StaticTest firstInstance = new StaticTest("testOne");
        System.out.println(firstInstance.getName() + " is instance number " + StaticTest.getNumInstances());

        StaticTest secondInstance = new StaticTest("testTwo");
        System.out.println(secondInstance.getName() + " is instance number " + secondInstance.getNumInstances());

        StaticTest thirdInstance = new StaticTest("testThree"); // the number will grow even is we use instances before
        System.out.println(thirdInstance.getName() + " is instance number " + secondInstance.getNumInstances());

        // java com.company.staticpractice.Main
        int answer = multiply(6);
        System.out.println("The answer is " + answer);

    }

    public static int multiply(int number){
       return number * multipler;
    }
}
