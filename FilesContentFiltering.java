import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.*;
import java.util.Scanner;
import java.util.HashMap;
import java.util.LinkedList;
import java.lang.Math;
import java.util.Collections;

class FilesContentFiltering {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private static final String INTEGERS_PATH = "integers.txt";
    private static final String FLOATS_PATH = "floats.txt";
    private static final String STRINGS_PATH = "strings.txt";

    // Global var for statistics
    private static HashMap<String, Integer> stats = new HashMap<>();

    private static LinkedList<Long> allNumbersInt = new LinkedList<>();

    private static LinkedList<Double> allNumbersFloat = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        boolean isShowStats = false;
        boolean isFullStats = false;
        boolean appendFlag = false;
        String path = "";
        String prefix = "";
        Pattern patternIsTxt = Pattern.compile("^.*\\.(txt)$");
        for(int i = 0; i < args.length; i++) {
            Matcher matcherIsTxt = patternIsTxt.matcher(args[i]);
            if(args[i].equals("-s")) {
                // System.out.println("SHORT STATS ACTIVE");
                isShowStats = true;
            }
            if (args[i].equals("-f")) {
                isShowStats = true;
                isFullStats = true;
            }
            if (args[i].equals("-a")) {
                // System.out.println("APPEND ACTIVE");
                appendFlag = true;
            }
            if (args[i].equals("-o")) {
                // System.out.println("SELECT OUTPUT PATH ACTIVE");
                if (i+1 >= args.length)
                    return;
                path = args[i+1] + "/";
                i++;
            }
            if (args[i].equals("-p")) {
                // System.out.println("PREFIX ACTIVE");
                if (i+1 >= args.length)
                    return;
                prefix = args[i+1];
                i++;
            }
            else if(matcherIsTxt.find()) {
                try {
                    contentFilter(args[i], appendFlag, path, prefix);
                    appendFlag = true;
                }
                catch (Exception e) {
                    System.out.println(ANSI_RED + "ОШИБКА" + ANSI_RESET + " файл " + args[i] + " отсутсвует");
                }
            }
       }
       if(isShowStats == true) {
           showStats(isFullStats);
       }
    }

    /**
     * Show statistics for filtering data
     *
     * @param isFull show full statistics?
     * @return print statistics in console
     */
    public static void showStats(boolean isFull) {
        if (isFull == true) {
            long sumInt = 0;
            double avgInt = 0;
            for (int i = 0; i < allNumbersInt.size(); i++) {
                sumInt += allNumbersInt.get(i);
            }
            Collections.sort(allNumbersInt);
            avgInt = sumInt/allNumbersInt.size();
            System.out.println("Минимальное целое число " + allNumbersInt.get(0));
            System.out.println("Максимальное целое число " + allNumbersInt.get(allNumbersInt.size()-1));
            System.out.println("Сумма целых чисел " + Long.toString(sumInt));
            System.out.println("Среднее целых чисел " + avgInt);

            double sumFloat = 0;
            double avgFloat = 0;
            for (int i = 0; i < allNumbersFloat.size(); i++) {
                sumFloat += allNumbersFloat.get(i);
            }
            Collections.sort(allNumbersFloat);
            avgFloat = sumFloat/allNumbersFloat.size();
            System.out.println("Минимальное вещественное число " + allNumbersFloat.get(0));
            System.out.println("Максимально вещественное число " + allNumbersFloat.get(allNumbersFloat.size()-1));
            System.out.println("Сумма вещественных чисел " + Double.toString(sumFloat));
            System.out.println("Среднее вещественных чисел " + avgFloat);
        }
        for (HashMap.Entry<String, Integer> entry: stats.entrySet()) {
                String key = entry.getKey();
                String value = Integer.toString(entry.getValue());
                System.out.println(key + " " + value + " lines saved");
        }
    }

    public static void contentFilter(String filename, boolean appendFlag, String path, String prefix) throws IOException {
        Scanner s = null;
        boolean appendFlagInt = appendFlag;
        boolean appendFlagFloat = appendFlag;
        boolean appendFlagString = appendFlag;
        try {
            s = new Scanner(new BufferedReader(new FileReader(filename)));
            s.useDelimiter("(\\n)");
            while (s.hasNext()) {
                if (s.hasNextLong()) {
                    try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path + prefix + INTEGERS_PATH, appendFlagInt)))) {
                        String line = s.next();

                        pw.println(line);

                        long li = Long.parseLong(line);

                        appendFlagInt = true;
                        stats.merge(INTEGERS_PATH, 1, Integer::sum);
                        allNumbersInt.add(li);
                        
                    }
                }
                else if (s.hasNextDouble()) {
                    try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path + prefix + FLOATS_PATH, appendFlagFloat)))) {
                        String line = s.next();

                        pw.println(line);

                        double d = Double.parseDouble(line);

                        appendFlagFloat = true;
                        stats.merge(FLOATS_PATH, 1, Integer::sum);
                        allNumbersFloat.add(d);
                    }
                }
                else {
                    try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path + prefix + STRINGS_PATH, appendFlagString)))) {
                        pw.println(s.next());
                        appendFlagString = true;
                        stats.merge(STRINGS_PATH, 1, Integer::sum);
                    }
                }
            }
        } finally {
            if (s != null) 
                s.close();
        }
    }
}
