package edu.ucf.cop4331c.dishdriver.models;

/**
 * Created by copper on 3/14/17.
 */

public class CredentialLoginModel {
    private String email;
    private String password;

    public CredentialLoginModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
