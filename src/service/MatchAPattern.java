package service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import common.MyConst;

public class MatchAPattern {

    // method to perform pattern matching on the predefined text file
    public static String perform(String p_pattern) {
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
        return printPatternResult(res);
    }

    // Method to print the result of pattern matching
    private static String printPatternResult(List<List<Integer>> resultFound) {
        String res = "";
        if (resultFound.size() > 0) {
            Iterator<List<Integer>> row = resultFound.iterator();
            while (row.hasNext()) {
                List<Integer> item = row.next();
                Iterator<Integer> itemItr = item.iterator();
                String temp2 = "Match found at position x on line x";
                while (itemItr.hasNext()) {
                    String pos = itemItr.next().toString();
                    temp2 = temp2.replaceFirst("x", pos);
                }
                res += temp2 + "\n";
                temp2 += "Match found at position x on line x";
            }
        } else {
            res = "No matches found\n";
        }
        return res;
    }
}