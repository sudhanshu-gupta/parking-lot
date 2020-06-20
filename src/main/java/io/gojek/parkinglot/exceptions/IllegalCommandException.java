package io.gojek.parkinglot.exceptions;

public class IllegalCommandException extends RuntimeException {

    public IllegalCommandException() {
        super("command not found");
    }
}
