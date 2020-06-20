package io.gojek.parkinglot.writer.impl;

import io.gojek.parkinglot.writer.PrintWriter;

public class CommandLinePrintWriter implements PrintWriter {

    @Override
    public void print(String s) {
        System.out.println(s);
    }

    @Override
    public void print(String s, Object... objects) {
        StringBuilder sb = new StringBuilder();
        int objPtr = 0;
        for (int i = 0; i < s.length(); i++) {
            if (i < s.length() - 1 && s.charAt(i) == '{' && s.charAt(i + 1) == '}' && objPtr < objects.length) {
                sb.append(objects[objPtr++]);
                i++;
            } else {
                sb.append(s.charAt(i));
            }
        }
        System.out.println(sb.toString());
    }
}
