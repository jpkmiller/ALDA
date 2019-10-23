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
    private static Dictionary<String, String> dic = new SortedArrayDictionary();

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
                        dic = create(1);
                    else
                        dic = create(0);
                    break;
                case "read":
                    if (EINGABE.hasNextInt())
                        dic = read(EINGABE.nextInt(), EINGABE.next());
                    read(EINGABE.next());
                    break;
                case "p":
                    assert dic != null;
                    print();
                    break;
                case "s":
                    assert dic != null;
                    search(EINGABE.next());
                    break;
                case "i":
                    assert dic != null;
                    insert(EINGABE.next(), EINGABE.next());
                    break;
                case "r":
                    assert dic != null;
                    remove(EINGABE.next());
                    break;
                case "exit":
                    exit();
                    break;
                case "mes":
                    searchMes(EINGABE.nextInt(), EINGABE.nextInt());
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
        for (Dictionary.Entry<String, String> e : dic)
            System.out.println(e.getKey() + ": " + e.getValue() + " search: " + dic.search(e.getKey()));
        System.out.println("Finished printing");
    }

    private static void search(String key) {
        System.out.println("Searching for " + key);
        dic.search(key);
    }

    private static void searchMes(int n, int m) {
        long start = 0, end = 0, diff;
        int c = 0;
        assert m <= dic.size();
        switch (n) {
            case 0:
                List<Dictionary.Entry<String, String>> l = new LinkedList<>();
                for (Dictionary.Entry<String, String> e : dic) {
                    l.add(e);
                }
                start = System.nanoTime();
                for (Dictionary.Entry<String, String> e : l) {
                    dic.search(e.getKey());
                }
                end = System.nanoTime();
                break;
            case 1:
                start = System.nanoTime();
                for (Dictionary.Entry<String, String> e : dic) {
                    dic.search(e.getValue());
                    if (++c >= m) break;
                }
                end = System.nanoTime();
                break;
        }
        diff = end - start;
        System.out.printf("Measured time %d nanoseconds\n", diff);
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
