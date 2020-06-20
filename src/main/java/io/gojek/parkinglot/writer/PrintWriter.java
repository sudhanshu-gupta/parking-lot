package io.gojek.parkinglot.writer;

public interface PrintWriter {

    void print(String s);

    void print(String s, Object... objects);
}
