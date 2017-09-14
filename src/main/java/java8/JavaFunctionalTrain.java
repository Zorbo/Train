package java8;


import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;

public class JavaFunctionalTrain {




    public static void main(String[] args) {


        final String number = "10+10";
        final String prefix = "MR. ";


        // Basically we wrote the METHOD BODY IN THE PARAMETER WITH LAMBDA
        System.out.println(processMore("HellO BEllO!", s -> s.toLowerCase())); //Same bellow
        System.out.println(processMore("HellO BEllO!", String::toLowerCase));
        System.out.println("**********************");
        System.out.println(processMore2("Hello World!", 5, (s, i) -> s.substring(i))); // Same bellow
        System.out.println(processMore2("Hello World!", 5, String::substring));
        System.out.println("***********************");
        System.out.println(processMore3("4",integer -> String.valueOf(integer))); // Same bellow
        System.out.println(processMore3("4", String::valueOf));
        System.out.println("************************");
        System.out.println(process("Hello World!", s -> prefix.concat(s))); //Same bellow
        System.out.println(process("Hello World!", prefix::concat));
        System.out.println("************************");

        System.out.println(process("Hello World!", s -> s.substring(3)));
        System.out.println(processMore("HellO BEllO!", s -> s.concat(number)));

        System.out.println(process("Hello World!", s -> {
            return prefix.concat(s);
        } ));

        String[] names = {"Ar Canjan", "Ms Arumpi", "Dr Bohn"};

        //Comparator interface can also be used for LAMBDAS!!
        Arrays.sort(names, (name1, name2) ->
                name1.split(" ")[1].compareTo(name2.split(" ")[1]));
        System.out.println(Arrays.toString(names));
        System.out.println("********Simple Lambda***********");
        Arrays.sort(names, Comparator.comparing(name -> name.split(" ")[1]));
        System.out.println(Arrays.toString(names));
        System.out.println("************Our own method Lambda using CLASSNAME**********");
        //We could also write our method and use it in Lambda with THE CLASS NAME!
        Arrays.sort(names, Comparator.comparing(JavaFunctionalTrain::firstName));
        System.out.println(Arrays.toString(names));
        System.out.println("*********Reversed*********");
        Arrays.sort(names, Comparator.comparing(JavaFunctionalTrain::firstName).reversed());
        System.out.println(Arrays.toString(names));
        System.out.println("*********Comparing the title TO!!!*********");
        Arrays.sort(names, Comparator.comparing(JavaFunctionalTrain::firstName).reversed().thenComparing(
                JavaFunctionalTrain::title
        ));
        System.out.println(Arrays.toString(names));
    }

    public static String title(String name){
        return name.split(" ")[0];
    }

    public static String firstName(String name){
        return name.split(" ")[1];
    }
                                                  // Function is Java8 interface for Lambdas
    private static String processMore3(String i, Function<Integer, String> processor) {
        return processor.apply(Integer.parseInt(i));
    }
                                                      // BitFunction is Java8 Interface for Lambdas
    private static String processMore2(String s, int i, BiFunction<String, Integer, String> processer) {
        return processer.apply(s, i);
    }

    // We made a Functional iterface for this method
    private static String process(String s, Processor processor) {
        return processor.process(s);
    }

    // IN JAVA THERE IS ALREADY A FUNCTIONAL INTERFACE AND WE CAN USE THAT
    private static String processMore(String s, Function<String, String> processor) {
        return processor.apply(s);
    }


    // Interface with a single matter called functional interface
    @FunctionalInterface
    interface Processor {
        String process(String s);
    }

}
