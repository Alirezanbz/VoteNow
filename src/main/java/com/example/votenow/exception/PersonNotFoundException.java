package com.example.votenow.exception;

public class PersonNotFoundException extends RuntimeException{
    public PersonNotFoundException(Long id){
        super("Person with id: '" + id + "' could not be found");
    }
}
