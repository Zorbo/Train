package udemy.collections.theatreold;

import java.util.HashMap;
import java.util.Map;

public class MapProgram {

    public static void main(String[] args) {

        Map<String, String> languages = new HashMap<>();
        if (languages.containsKey("Java")){
            System.out.println("Java already exist");
        } else {
            languages.put("Java", "a compiled high leve, object orianted, platform independent language");
            System.out.println("Java added successfully");
        }

        languages.put("Python","an interpreted, object-orianted, high-level programming language with dynamic semantics");
        languages.put("Algol", "an algorithmic language");
        System.out.println(languages.put("BASIC","Beginners All Purposes Symbolic Instruction Code"));
        System.out.println(languages.put("Lisp","Therein lies madness"));


        if (languages.containsKey("Java")){
            System.out.println("Java is already in the map");
        } else {
            languages.put("Java","this course is about java");
        }

        System.out.println("======================================");

       // languages.remove("Lisp");
        if (languages.remove("Algol", "an algorithmic language")){
            System.out.println("Algol removed");
        } else {
            System.out.println("Algol not removed, key value pair not found");
        }

        if (languages.replace("Lisp","Therein lies madness","A functional programming language with imperative features")){
            System.out.println("Lisp replaced");
        } else {
            System.out.println("Lisp is not replaced");
        }
     //   System.out.println(languages.replace("Scala", "This will not be added"));

        for (String key : languages.keySet()){
            System.out.println(key + " : " + languages.get(key));
        }

    }
}
