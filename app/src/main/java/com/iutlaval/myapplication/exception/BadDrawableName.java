package com.iutlaval.myapplication.exception;

public class BadDrawableName extends RuntimeException{
    String name;
    public BadDrawableName(String name) {
        this.name=name;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
        if(name == null)
        {
            System.out.println("NAME CAN'T BE NULL");
        }else{
            System.out.println("NAME CAN'T BE EMPTY");
        }
    }
}
