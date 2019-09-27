package io.github.isayes.shanbaydemo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetWordsList {

    private Map<String, Integer> listOfWords = new HashMap<>();
    public List<String> wordsOfLevel_1 = new ArrayList<>();
    public List<String> wordsOfLevel_2 = new ArrayList<>();
    public List<String> wordsOfLevel_3 = new ArrayList<>();
    public List<String> wordsOfLevel_4 = new ArrayList<>();
    public List<String> wordsOfLevel_5 = new ArrayList<>();

    private GetWordsList() {
    }

    private static GetWordsList getWordsList = new GetWordsList();
    public static GetWordsList getInstance() {
        return getWordsList;
    }

    public boolean Load(InputStream is) {
        try {
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            line = br.readLine();
            while (line != null) {
                String[] strArr = line.split("\t");
                int level = Integer.valueOf(strArr[1]);
                listOfWords.put(strArr[0], level);
                switch (level) {
                    case 1:
                        wordsOfLevel_1.add(strArr[0]);
                        break;
                    case 2:
                        wordsOfLevel_2.add(strArr[0]);
                        break;
                    case 3:
                        wordsOfLevel_3.add(strArr[0]);
                        break;
                    case 4:
                        wordsOfLevel_4.add(strArr[0]);
                        break;
                    case 5:
                        wordsOfLevel_5.add(strArr[0]);
                        break;
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
