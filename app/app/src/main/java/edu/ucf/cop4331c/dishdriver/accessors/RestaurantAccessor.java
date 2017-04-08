package edu.ucf.cop4331c.dishdriver.accessors;

import edu.ucf.cop4331c.dishdriver.models.RestaurantModel;

/**
 * Created by viviennedo on 4/4/17.
 */

public class RestaurantAccessor {
    private static RestaurantModel sRestaurantModel;

    public static RestaurantModel getInstance() {
        if (sRestaurantModel == null) {
            // Create restaurant Model.
        }

        return sRestaurantModel;
    }


}
