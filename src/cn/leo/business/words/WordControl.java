package cn.leo.business.words;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordControl {
    private static List<String> lines = new ArrayList<>();

    static {
        File wordFile = new File("words");
        LineNumberReader lnr = null;
        try {
            InputStream is = new FileInputStream(wordFile);
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            lnr = new LineNumberReader(isr);
            String line = null;
            while ((line = lnr.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (lnr != null) {
                    lnr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getWord() {
        Random random = new Random();
        int i = random.nextInt(lines.size());
        return lines.get(i);
    }
}
