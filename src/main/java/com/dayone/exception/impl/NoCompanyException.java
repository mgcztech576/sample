package com.dayone.exception.impl;
import com.dayone.exception.AbstractException;
import org.springframework.http.HttpStatus;
public class NoCompanyException extends AbstractException {
    @Override public int getStatusCode(){
        return HttpStatus.BAD_REQUEST.value();}
    @Override public String getMessage(){return "없는 회사명";}}