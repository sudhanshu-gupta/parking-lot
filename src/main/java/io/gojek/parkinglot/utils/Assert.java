package io.gojek.parkinglot.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Assert {

    public void notNull(Object value, String message) {
        if(null == value) {
            throw new IllegalArgumentException(message);
        }
    }

    public void notEmpty(String value, String message) {
        if(null == value || value.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public void equals(int actual, int expected, String message) {
        if(actual != expected) {
            throw new IllegalArgumentException(message);
        }
    }

    public void greaterThanOrEqualTo(int value, int minimum, String message) {
        if(value < minimum) {
            throw new IllegalArgumentException(message);
        }
    }
}
