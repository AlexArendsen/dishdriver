package edu.ucf.cop4331c.dishdriver.models;

import retrofit2.Call;

/**
 * Created by copper on 3/14/17.
 */

public class RegistrantModel extends UserModel {
    private String password;

    // region Constructors
    public RegistrantModel(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }

    public RegistrantModel(String email, String password, String fname, String lname) {
        super();
        this.email = email;
        this.password = password;
        this.firstName = fname;
        this.lastName = lname;
    }

    public RegistrantModel(UserModel user) {
        this.email = user.email;
        this.password = user.password;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
    }
    // endregion

    // region Getters and Setters
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }
    // endregion

    public Call<Integer> create() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public Call<Boolean> update() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }


}
