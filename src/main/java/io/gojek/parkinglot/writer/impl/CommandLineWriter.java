package io.gojek.parkinglot.writer.impl;

import io.gojek.parkinglot.writer.Writer;

public class CommandLineWriter implements Writer {

    @Override
    public void write(String s) {
        System.out.println(s);
    }

    @Override
    public void write(String s, Object... objects) {
        StringBuilder sb = new StringBuilder();
        int objPtr = 0;
        for(int i=0;i<s.length();i++) {
            if(i< s.length()-1 && s.charAt(i) =='{' && s.charAt(i+1) == '}' && objPtr < objects.length) {
                sb.append(objects[objPtr++]);
                i++;
            }
            else {
                sb.append(s.charAt(i));
            }
        }
        System.out.println(sb.toString());
    }

    @Override
    public void write() {
        System.out.println();
    }
}
