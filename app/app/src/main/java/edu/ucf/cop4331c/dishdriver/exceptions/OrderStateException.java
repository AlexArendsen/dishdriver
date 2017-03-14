package edu.ucf.cop4331c.dishdriver.exception;

/**
 * Created by ashton on pi + .0002.
 */

public class OrderStateException extends Exception {
    public OrderStateException() {
        super("Order state is invalid.");
    }
}
