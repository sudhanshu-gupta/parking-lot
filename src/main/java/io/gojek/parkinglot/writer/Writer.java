package io.gojek.parkinglot.writer;

public interface Writer {

    void write(String s);

    void write(String s, Object...objects);

    void write();
}
