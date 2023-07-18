package com.example.votenow.exception;

public class FrageNotFoundException extends RuntimeException{
    public FrageNotFoundException(Long id){
        super("Frage with id: '" + id + "' could not be found");
    }
}
