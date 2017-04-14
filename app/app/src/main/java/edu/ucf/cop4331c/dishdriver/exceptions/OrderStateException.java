package edu.ucf.cop4331c.dishdriver.exceptions;

/**
 * Created by ashton on pi + .0002.
 */

//  throws OrderStateException{ throw new OrderStateException; }

public class OrderStateException extends Exception {
    public OrderStateException() {
        super("Order state is invalid.");
    }
}
