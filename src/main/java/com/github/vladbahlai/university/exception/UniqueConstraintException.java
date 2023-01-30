package com.github.vladbahlai.university.exception;

public class UniqueConstraintException extends Exception{
    public UniqueConstraintException(String message) {
        super(message);
    }
}
