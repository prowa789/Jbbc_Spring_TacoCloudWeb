package com.hust.tacocloud;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class FilterStreamExample {
    public static void main(String[] args) {
        List<String> data = Arrays.asList("Java", "C#", "C++", "PHP", "Javascript");

        data.stream() //
                .skip(1) //
                .limit(3) //
                .forEach(System.out::print); // C#C++PHP
    }
}
