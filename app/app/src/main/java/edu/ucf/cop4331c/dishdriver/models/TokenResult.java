package edu.ucf.cop4331c.dishdriver.models;

/**
 * Created by copper on 3/14/17.
 */

public class TokenResult {
    private String token;

    public TokenResult(String token) {
        this.token = token;
    }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }
}
