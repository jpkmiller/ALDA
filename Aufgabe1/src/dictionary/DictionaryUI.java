package dictionary;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DictionaryUI {

    private static final Scanner EINGABE = new Scanner(System.in);
    private static Dictionary<String, String> dic = new SortedArrayDictionary<>();

    public static void main(String[] args) {
        handler();
    }

    private static void handler() {
        String in;
        while (EINGABE.hasNext()) {
            in = EINGABE.next();
            switch (in) {
                case "create":
                    switch (EINGABE.next()) {
                        case "binary":
                            dic = create(2);
                            break;
                        case "hash":
                            dic = create(1);
                            break;
                        default:
                            dic = create(0);
                    }
                    break;
                case "read":
                    if (EINGABE.hasNextInt())
                        dic = read(EINGABE.nextInt(), EINGABE.next());
                    else
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
                case "mes":
                    searchMes(EINGABE.nextInt(), EINGABE.nextInt());
                    break;
                default:
                    break;
            }
        }
    }

    private static Dictionary<String, String> create(int i) {
        System.out.print("Creating ");
        if (i == 2) {
            System.out.print("BinaryTree Dictionary\n");
            return new BinaryTreeDictionary<>();
        } else if (i == 1) {
            System.out.print("Hash Dictionary\n");
            return new HashDictionary<>(3, 31);
        }
        System.out.print("SortedArray Dictionary\n");
        return new SortedArrayDictionary<>();
    }

    private static void read(String next) {
        read(-1, next);
    }

    private static Dictionary<String, String> read(int n, String f) {
        System.out.println("Reading Dictionary");
        long start = 0, end = 0;
        if (dic == null)
            create(0);
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
        long diff = end - start;
        System.out.printf("Measured time %d milliseconds\n", diff);
        return dic;
    }

    private static void print() {
        System.out.println("Printing Dictionary");
        if (dic instanceof BinaryTreeDictionary)
            pp();
        else
            for (Dictionary.Entry<String, String> e : dic)
                System.out.println(e.getKey() + ": " + e.getValue() + " search: " + dic.search(e.getKey()));
        System.out.println("Finished printing");
    }

    private static void search(String key) {
        System.out.println("Searching for " + key);
        String s;
        if ((s = dic.search(key)) != null)
            System.out.printf("Search result: %s\n", s);
        else
            System.out.println("No entry found");
    }


    private static void searchMes(int n, int m) {
        long start = 0, end = 0, diff;
        int c = 0;
        List<String> l = new LinkedList<>();
        switch ( n) {
            case 0:
                for (Dictionary.Entry<String, String> e : dic)
                    l.add(e.getKey());
                break;
            case 1:
                for (Dictionary.Entry<String, String> e : dic)
                    l.add(e.getValue());
                break;
        }
        start = System.nanoTime();
        for (String e : l) {
            dic.search(e);
            if (++c >= m) break;
        }
        end = System.nanoTime();
        diff = end - start;
        System.out.printf("Measured time %d nanoseconds\n", diff);
    }

    private static void pp() {
        if (dic instanceof BinaryTreeDictionary)
            ((BinaryTreeDictionary<String, String>) dic).prettyPrint();
    }

    private static void insert(String key, String value) {
        System.out.println("Inserting " + key + " and " + value);
        dic.insert(key, value);
    }

    private static void remove(String key) {
        System.out.println("Removing " + key);
        dic.remove(key);
    }

    private static void exit() {
        System.out.println("Bye");
        System.exit(0);
    }
}
