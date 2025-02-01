package com.example.shopApp_backend.exceptions;

public class InvalidParamException extends Exception{
    public InvalidParamException (String message){
        super(message);
    }
}
