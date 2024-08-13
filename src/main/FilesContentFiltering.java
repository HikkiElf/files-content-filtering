import java.io.*;
import java.nio.file.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Scanner;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Collections;
import com.hikkielf.Allert;

class FilesContentFiltering {

    // Files names for filtered data
    private static final String INTEGERS_PATH = "integers.txt";
    private static final String FLOATS_PATH = "floats.txt";
    private static final String STRINGS_PATH = "strings.txt";

    // Global vars for statistics
    private static HashMap<String, Integer> stats = new HashMap<>();
    private static LinkedList<Long> allNumbersInt = new LinkedList<>();
    private static LinkedList<Double> allNumbersFloat = new LinkedList<>();
    private static LinkedList<String> allStrings = new LinkedList<>();

    public static void main(String[] args) throws IOException {

        boolean isShowStats = false;
        boolean isFullStats = false;
        boolean isAppend = false;

        String path = "";
        String prefix = "";

        Pattern patternIsTxt = Pattern.compile("^.*\\.(txt)$");
        Pattern patternIsDir = Pattern.compile("(/)");

        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("-s")) {
                isShowStats = true;
            }
            else if (args[i].equals("-f")) {
                isShowStats = true;
                isFullStats = true;
            }
            else if (args[i].equals("-a")) {
                isAppend = true;
            }
            else if (args[i].equals("-o")) {
                if (i+1 >= args.length) {
                    return;
                }
                path = args[i+1] + "/";
                Path checkPath = Paths.get(path);
                if (!Files.isDirectory(checkPath)) {
                    Allert.error("директория не найдена!");
                    return;
                }
                i++;
            }
            else if (args[i].equals("-p")) {
                if (i+1 >= args.length) {
                    return;
                }
                Matcher matcherIsDir = patternIsDir.matcher(args[i + 1]);
                if(matcherIsDir.find()) {
                    Allert.warning("Недопустимый знак в префиксе '/' заменён на '-'");
                }
                prefix = args[i + 1].replaceAll("(/)", "-");
                i++;
            }
            else {
                Matcher matcherIsTxt = patternIsTxt.matcher(args[i]);
                if(matcherIsTxt.find()) {
                    try {
                        contentFilter(args[i], isAppend, path, prefix);
                        isAppend = true;
                    }
                    catch (Exception e) {
                        Allert.warning("файл " + args[i] + " отсутсвует");
                        System.out.println(e);
                    }
                }
                else {
                    Allert.warning(args[i] + " не распознано как файл или команда");
                }
            }
       }
       if(isShowStats == true) {
           showStats(isFullStats);
       }
    }

    private static void intStats() {
        if(allNumbersInt.size() != 0) {
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
        }
    }

    private static void floatStats() {
        if(allNumbersFloat.size() != 0) {
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
    }

    private static void stringStats() {
        if (allStrings.size() != 0) {
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;

            String minString = "";
            String maxString = "";
            
            Collections.sort(allStrings);
            for(int i = 0; i<allStrings.size(); i++) {
                if(allStrings.get(i).length() < min) {
                    minString = allStrings.get(i);
                    min = allStrings.get(i).length();
                }
                if (allStrings.get(i).length() > max) {
                    maxString = allStrings.get(i);
                    max = allStrings.get(i).length();
                }
            }
            System.out.println("Самая короткая строка: " + minString);
            System.out.println("Самая длинная строка: " + maxString);
        }
    }

    private static void baseStats() {
        for (HashMap.Entry<String, Integer> entry: stats.entrySet()) {
            String key = entry.getKey();
            String value = Integer.toString(entry.getValue());
            String wordEnd = "";
            String wordEndSave = "о";
            if (Integer.parseInt(value) % 10 == 1){
                wordEnd = "а";
                wordEndSave = wordEnd;
            }
            else if (Integer.parseInt(value) % 10 < 5)
                wordEnd = "и";
            System.out.println(key + " " + value + " строк" + wordEnd + " сохранен" + wordEndSave);
        }
    }

    /**
     * Show statistics for filtered data
     *
     * @param isFull show full statistics?
     * @return print statistics in console
     */
    private static void showStats(boolean isFull) {
        if (isFull == true) {
            intStats();

            floatStats();

            stringStats();
        }
        baseStats();
    }

    /**
     * Filtering and write data to .txt files
     *
     * @param filename
     * @param isAppend append data if true, rewrite if false
     * @param path path for output files 
     * @param prefix prefix for output filenames
     * @return write filtered data in .txt file
     */
    public static void contentFilter(String filename, boolean isAppend, String path, String prefix) throws IOException {

        boolean appendFlagInt = isAppend;
        boolean appendFlagFloat = isAppend;
        boolean appendFlagString = isAppend;

        try(Scanner s = new Scanner(new BufferedReader(new FileReader(filename)))) {

            // new line delimeter
            s.useDelimiter("(\\n)");
            while (s.hasNext()) {
                if (s.hasNextLong()) {
                    try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path + prefix + INTEGERS_PATH, appendFlagInt)))) {
                        String line = s.next();

                        pw.println(line);

                        long li = Long.parseLong(line);

                        appendFlagInt = true;
                        stats.merge(prefix + INTEGERS_PATH, 1, Integer::sum);
                        allNumbersInt.add(li);
                        
                    }
                }
                else if (s.hasNextDouble()) {
                    try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path + prefix + FLOATS_PATH, appendFlagFloat)))) {
                        String line = s.next();

                        pw.println(line);

                        double d = Double.parseDouble(line);

                        appendFlagFloat = true;
                        stats.merge(prefix + FLOATS_PATH, 1, Integer::sum);
                        allNumbersFloat.add(d);
                    }
                }
                else {
                    try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path + prefix + STRINGS_PATH, appendFlagString)))) {
                        String line = s.next();

                        pw.println(line);

                        appendFlagString = true;
                        stats.merge(prefix + STRINGS_PATH, 1, Integer::sum);
                        allStrings.add(line);
                    }
                }
            }
        }     
    }
}
