package edu.ucf.cop4331c.dishdriver.exceptions;

/**
 * Created by ashton on pi + .0002.
 */

//  throws UnsupportedOperationException{ throw new UnsupportedOperationException; }

public class UnsupportedOperationException extends Exception {
    public UnsupportedOperationException() {
        super("Unsupported operation.");
    }
}