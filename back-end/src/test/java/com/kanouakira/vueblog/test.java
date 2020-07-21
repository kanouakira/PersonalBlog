package com.kanouakira.vueblog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;

public class test {

    public static void main(String[] args) {
        String text = "Keep calm and code on";
        String[] result = text.split(" ");
        ArrayList<String> strings = new ArrayList<>(Arrays.asList(result));
        int minStringLen = 0;
        StringBuffer minStringBuffer = new StringBuffer();

        for (int i = 0; i < strings.size()-1; i++) {
            minStringLen = strings.get(i).length();

            for (int j = i+1; j < strings.size(); j++) {
                System.out.println(i+" _ "+j);
                if (minStringLen > strings.get(j).length()){
                    minStringBuffer.setLength(0);
                    minStringLen = strings.get(j).length();
                    minStringBuffer.append(result[j]);
                }else if (minStringLen == strings.get(j).length()){
                    minStringBuffer.append(result[j]);
                }
            }
            System.out.println(minStringBuffer.toString());
        }
    }
}