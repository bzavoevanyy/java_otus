package ru.otus;

import com.google.common.base.Splitter;

public class HelloOtus {
    public static void main(String[] args) {
        String inputString = "key1-value1; key2-value2; key3-value3";
        var resultMap = Splitter.on(";").trimResults().withKeyValueSeparator("-").split(inputString);
        System.out.println(resultMap);
    }
}
