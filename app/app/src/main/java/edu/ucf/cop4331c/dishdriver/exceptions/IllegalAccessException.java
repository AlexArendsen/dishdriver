package edu.ucf.cop4331c.dishdriver.exceptions;

/**
 * Created by ashton on pi + .0002.
 */

//  throws IllegalAccessException{ throw new IllegalAccessException; }

public class IllegalAccessException extends Exception {
    public IllegalAccessException() {
        super("Your credintials are invalid.");
    }
}
