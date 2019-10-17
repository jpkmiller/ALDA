package dictionary;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DictionaryUI {

    private static final Scanner EINGABE = new Scanner(System.in);

    public static void main(String[] args) {
        handler();
    }

    private static void handler() {
        Dictionary dic = null;
        String in;
        while (EINGABE.hasNext()) {
            in = EINGABE.next();
            switch (in) {
                case "create":
                    if (EINGABE.hasNext() && EINGABE.next().equals("hash"))
                        dic = create(1);
                    else
                        dic = create(0);
                    break;
                case "read":
                    if (EINGABE.hasNextInt())
                        dic = read(EINGABE.nextInt(), EINGABE.next(), dic);
                    read(EINGABE.next(), dic);
                    break;
                case "p":
                    print(dic);
                    break;
                case "s":
                    search(EINGABE.next(), dic);
                    break;
                case "i":
                    insert(EINGABE.next(), EINGABE.next(), dic);
                    break;
                case "r":
                    remove(EINGABE.next(), dic);
                    break;
                case "exit":
                    exit();
                    break;
                default:
            }

        }
    }

    private static Dictionary create(int i) {
        System.out.println("Creating Dictionary");
        if (i == 1)
            return new HashDictionary(3, 31);
        else
            return new SortedArrayDictionary();
    }

    private static void read(String next, Dictionary<String, String> dic) {
            read(-1, next, dic);
    }

    private static Dictionary<String, String> read(int n, String f, Dictionary<String, String> dic) {
        System.out.println("Reading Dictionary");
        long start = 0, end = 0;
        if (dic == null)
            dic = create(0);
        try {
            LineNumberReader in = new LineNumberReader(new FileReader(f));
            String line;
            start = System.currentTimeMillis();
            for (int i = 0; (line = in.readLine()) != null; ++i) {
                String[] sf = line.split(" ");
                if (n == -1 || n > i)
                    dic.insert(sf[0], sf[1]);
                else if (n == i)
                    break;
            }
            end = System.currentTimeMillis();
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.printf("Measured time %d milliseconds\n", end - start);
        return dic;
    }

    private static void print(Dictionary<String, String> dic) {
        System.out.println("Printing Dictionary");
        for (Dictionary.Entry<String, String> e : dic)
            System.out.println(e.getKey() + ": " + e.getValue() + " search: " + dic.search(e.getKey()));
        System.out.println("Finished printing");
    }

    private static void search(String key, Dictionary<String, String> dic) {
        System.out.println("Searching for " + key);
        dic.search(key);
    }

    private static void insert(String key, String value, Dictionary<String, String> dic) {
        System.out.println("Inserting " + key + " and " + value);
        dic.insert(key, value);
    }

    private static void remove(String key, Dictionary<String, String> dic) {
        System.out.println("Removing " +  key);
        dic.remove(key);
    }

    private static void exit() {
        System.out.println("Bye");
        System.exit(0);
    }
}
