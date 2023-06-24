package service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

import common.MyConst;

public class MatchAPattern {
    public static List<List<Integer>> perform(String p_pattern) {
        List<List<Integer>> res = new LinkedList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(MyConst.MOON_FILE));
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                // Search for pattern in line (ignore case)
                int index = line.toLowerCase().indexOf(p_pattern);
                while (index >= 0) {
                    List<Integer> item = new LinkedList<>();
                    item.add(index + 1);
                    item.add(lineNumber);
                    res.add(item);
                    // Print position number of match
                    index = line.toLowerCase().indexOf(p_pattern, index + 1); // check for next
                    // match
                }
                lineNumber++;
            }
        } catch (Exception e) {
            System.out.println("File not found.");
        }
        return res;
    }
}