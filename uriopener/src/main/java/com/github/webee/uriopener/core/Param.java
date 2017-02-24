package com.github.webee.uriopener.core;

/**
 * Created by webee on 17/2/20.
 */

public class Param {
    public final String name;
    public final String value;

    public Param(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("Param{%s => %s}", name, value);
    }
}
