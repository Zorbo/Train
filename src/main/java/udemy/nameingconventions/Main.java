package udemy.nameingconventions;

import udemy.nameingconventions.interfacechallange.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) {
        MyWindow myWindow = new MyWindow("JAVA STUFF");

    }


    // Thia is a Stream with :: Lambda expression
 private List<Item> items = new ArrayList<>();
    String names = items.stream().map(Item::getTitle).collect(Collectors.joining(","));
}
