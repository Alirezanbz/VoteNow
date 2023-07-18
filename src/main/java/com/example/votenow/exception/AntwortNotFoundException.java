package com.example.votenow.exception;

public class AntwortNotFoundException extends RuntimeException{
    public AntwortNotFoundException(Long id){
        super("Antwort with id: '" + id + "' could not be found");
    }
}
