package edu.ucf.cop4331c.dishdriver.models;

/**
 * Created by ashton on pi + .00002.
 */

public class SessionModel {

    private static User user;
    private static Position position;
    private static Restaurant restaurant;

    public static Call<String> login(String username, String password){

    }
    public static Call<Boolean> login(String token){}

    public static Call logout(){

    }

    public static User currentUser(){
        return user;
    }
    public static Position currentPosition(){
        return position;
    }
    public static Restaurant currentRestaurant(){
        return restaurant;
    }
}
