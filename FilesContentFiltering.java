import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.*;
import java.util.Scanner;

class FilesContentFiltering {
    public static void main(String[] args) throws IOException {
        boolean appendFlag = false;
        String path = "";
        String prefix = "";
        Pattern patternIsTxt = Pattern.compile("^.*\\.(txt)$");
        for(int i = 0; i < args.length; i++) {
            Matcher matcherIsTxt = patternIsTxt.matcher(args[i]);
            if (args[i].equals("-a")) {
                System.out.println("APPEND FLAG ACTIVE");
                appendFlag = true;
            }
            if (args[i].equals("-o")) {
                System.out.println("SELECT OUTPUT PATH ACTIVE");
                if (i+1 >= args.length)
                    return;
                path = args[i+1] + "/";
                i++;
            }
            if (args[i].equals("-p")) {
                System.out.println("PREFIX ACTIVE");
                if (i+1 >= args.length)
                    return;
                prefix = args[i+1];
                i++;
            }
            else if(matcherIsTxt.find()) {
                System.out.println(args[i]);
                contentFilter(args[i], appendFlag, path, prefix);
                appendFlag = true;
          }
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
                    try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path + prefix + "integers.txt", appendFlagInt)))) {
                        pw.println(s.next());
                        appendFlagInt = true;
                    }
                }
                else if (s.hasNextDouble()) {
                    try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path + prefix + "floats.txt", appendFlagFloat)))) {
                        pw.println(s.next());
                        appendFlagFloat = true;
                    }
                }
                else {
                    try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path + prefix + "strings.txt", appendFlagString)))) {
                        pw.println(s.next());
                        appendFlagString = true;
                    }
                }
            }
        } finally {
            if (s != null) 
                s.close();
        }
    }
}
