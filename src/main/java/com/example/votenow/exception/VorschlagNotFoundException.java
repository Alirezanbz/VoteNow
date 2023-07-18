package com.example.votenow.exception;

public class VorschlagNotFoundException extends RuntimeException{
    public VorschlagNotFoundException(Long id){
        super("Vorschlag with id: '" + id + "' could not be found");
    }
}
