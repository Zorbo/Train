package udemy.scope.scopechallange;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        X x = new X();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a number: " );
        x.x(scanner.nextInt());

    }

}
