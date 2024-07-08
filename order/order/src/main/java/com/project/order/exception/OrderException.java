package com.project.order.exception;

public class OrderException extends Exception{

    public OrderException(String message) {
        super(message);
    }
    
    public OrderException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public OrderException(Throwable cause) {
        super(cause);
    }

}
