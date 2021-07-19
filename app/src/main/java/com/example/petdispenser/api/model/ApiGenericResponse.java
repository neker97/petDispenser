package com.example.petdispenser.api.model;

public class ApiGenericResponse<T> {
    public Boolean success;
    public T result;
    public String error;
}

