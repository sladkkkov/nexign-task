package ru.sladkkov.dtoshare.enums;

import java.util.List;

public enum TypeCall {
    OUTGOING("01"),
    INCOMING("02");

    public static final List<TypeCall> TYPE_CALLS = List.of(TypeCall.values());
    private final String numericTypeCall;

    TypeCall(String numericTypeCall) {
        this.numericTypeCall = numericTypeCall;
    }

    public static TypeCall fromNumericNameOfType(String numericNameOfType) {

        if (numericNameOfType.equals("01")) {
            return OUTGOING;
        }

        return INCOMING;
    }

    public String getNumericTypeCall() {
        return numericTypeCall;
    }
}
