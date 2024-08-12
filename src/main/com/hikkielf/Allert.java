package com.hikkielf;
import com.hikkielf.Color;

public class Allert {

    public static void warning(String message) {
        System.out.println(Color.colorize(Color.yellow, "ВНИМАНИЕ ") + message);
    }

    public static void error(String message) {
        System.out.println(Color.colorize(Color.red, "ОШИБКА ") + message);
    }
}
