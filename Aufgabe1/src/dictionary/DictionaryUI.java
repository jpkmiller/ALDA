package dictionary;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DictionaryUI {

    static Dictionary<String, String> dic;

    public static void main(String[] args) {
        handler(args);
    }

    private static void handler(String[] arg) {
        for (int i = 0; i < arg.length; i++) {
            switch (arg[i]) {
                case "create":
                    if (arg[i + 1].equals("hash"))
                        create(new HashDictionary(3));
                    else
                        create(new SortedArrayDictionary());
                    break;
                case "read":
                    if (arg[i + 1].matches("d+"))
                        read(Integer.parseInt(arg[i + 1]), arg[i + 2]);
                    else read(arg[i + 1]);
                    break;
                case "p":
                    print();
                    break;
                case "s":
                    search(arg[i + 1]);
                    break;
                case "i":
                    insert(arg[i + 1], arg[i + 2]);
                    break;
                case "r":
                    remove(arg[i + 1]);
                    break;
                case "exit":
                    exit();
                    break;
            }
        }
    }

    private static void create(Dictionary dic) {
        DictionaryUI.dic = dic;
    }

    private static void read(String f) {
        read(-1, f);
    }

    private static void read(int n, String f) {
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
        for (Dictionary.Entry<String, String> e : dic) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }
    }

    private static void search(String key) {
        dic.search(key);
    }

    private static void insert(String key, String value) {
        dic.insert(key, value);
    }

    private static void remove(String key) {
        dic.remove(key);
    }

    private static void exit() {
        System.exit(0);
    }
}
