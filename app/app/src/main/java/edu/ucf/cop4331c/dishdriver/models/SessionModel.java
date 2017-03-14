package edu.ucf.cop4331c.dishdriver.models;

/**
 * Created by ashton on pi + .0002.
 */

public class SessionModel {

    private static User user;
    private static Position position;
    private static Restaurant restaurant;

    public static Call<String> login(String username, String password) throws UnsupportedOperationException{ throw new UnsupportedOperationException; }
    public static Call<Boolean> login(String token) throws UnsupportedOperationException{ throw new UnsupportedOperationException; }
    public static Call logout() throws UnsupportedOperationException{ throw new UnsupportedOperationException; }

    public static User currentUser(){
        return user;
    }
    public static PositionModel currentPosition(){
        return position;
    }
    public static RestaurantModel currentRestaurant(){
        return restaurant;
    }
}
