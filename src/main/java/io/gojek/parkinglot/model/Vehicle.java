package io.gojek.parkinglot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public abstract class Vehicle implements Serializable {

    private final String registrationNo;
    private final String color;
}
