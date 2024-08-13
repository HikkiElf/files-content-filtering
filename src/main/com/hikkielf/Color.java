package com.hikkielf;

public enum Color {
        
    ANSI_RESET ("\u001B[0m"),
    red ("\u001B[31m"),    
    green ("\u001B[32m"),
    yellow ("\u001B[33m"),    
    blue ("\u001B[34m"),       
    purple ("\u001B[35m"),    
    cyan ("\u001B[36m"),      
    white ("\u001B[37m");

    private final String code;

    Color(String code) {
        this.code = code;
    }

    public static String colorize(Color color, Object message) {
        return color.code + message + Color.ANSI_RESET.code;
    }
}
