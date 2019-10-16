package dictionary;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DictionaryUI {

    static Dictionary<String, String> dic;
    static final Scanner EINGABE = new Scanner(System.in);

    public static void main(String[] args) {
        handler();
    }

    private static void handler() {
        String in;
        while (EINGABE.hasNext()) {
            in = EINGABE.next();
            switch (in) {
                case "create":
                    if (EINGABE.hasNext() && EINGABE.next().equals("hash"))
                        create(new HashDictionary(3));
                    else
                        create(new SortedArrayDictionary());
                    break;
                case "read":
                    if (EINGABE.hasNextInt()) {
                        read(EINGABE.nextInt(), EINGABE.next());
                    }
                        read(EINGABE.next());
                    break;
                case "p":
                    print();
                    break;
                case "s":
                    search(EINGABE.next());
                    break;
                case "i":
                    insert(EINGABE.next(), EINGABE.next());
                    break;
                case "r":
                    remove(EINGABE.next());
                    break;
                case "exit":
                    exit();
                    break;
                default:
            }

        }
    }

    private static void read(String next) {
        read(-1, next);
    }

    private static void create(Dictionary dic) {
        System.out.println("Creating Dictionary");
        DictionaryUI.dic = dic;
    }

    private static void read(int n, String f) {
        System.out.println("Reading Dictionary");
        LineNumberReader in;
        try {
            in = new LineNumberReader(new FileReader(f));
            String line;
            int i = 0;
            while ((line = in.readLine()) != null) {
                ++i;
                String[] sf = line.split(" ");
                DictionaryUI.dic = new SortedArrayDictionary<>();
                if (n == -1 || n > i)
                    dic.insert(sf[0], sf[1]);
                else
                    break;
            }
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void print() {
        System.out.println("Printing Dictionary");
        for (Dictionary.Entry<String, String> e : dic) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }
    }

    private static void search(String key) {
        System.out.println("Searching for " + key);
        dic.search(key);
    }

    private static void insert(String key, String value) {
        System.out.println("Inserting " + key + " and " + value);
        dic.insert(key, value);
    }

    private static void remove(String key) {
        System.out.println("Removing " +  key);
        dic.remove(key);
    }

    private static void exit() {
        System.out.println("Bye");
        System.exit(0);
    }
}
