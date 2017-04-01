package edu.ucf.cop4331c.dishdriver;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import edu.ucf.cop4331c.dishdriver.models.PositionModel;
import edu.ucf.cop4331c.dishdriver.models.RestaurantModel;
import rx.Observable;
import rx.observers.TestSubscriber;


/**
 * Created by copper on 3/29/17.
 */

public class PositionModelTests {

    @Test
    public void GetPositionsForPOM() throws Exception {

        // ... I mean look at all this friggin boilerplate.
        // isn't it neat?
        // Java: "wouldn't you think my java.util.Collection was complete?"
        //
        // ... no. No, this is just one test case T_T

        Observable<List<RestaurantModel>> restaurantObservable = RestaurantModel.get(4);
        TestSubscriber<List<RestaurantModel>> restaurantSubscriber = new TestSubscriber<>();
        restaurantObservable.subscribe(restaurantSubscriber);

        restaurantSubscriber.assertNoErrors();
        restaurantSubscriber.awaitTerminalEvent();

        List<List<RestaurantModel>> restaurants = restaurantSubscriber.getOnNextEvents();

        Assert.assertFalse("Could not find the restaurant!", restaurants.get(0).isEmpty());

        RestaurantModel theRestaurant = restaurants.get(0).get(0);

        Observable<List<PositionModel>> pObservable = PositionModel.forRestaurant(theRestaurant);
        TestSubscriber<List<PositionModel>> pSubscriber = new TestSubscriber<>();
        pObservable.subscribe(pSubscriber);

        pSubscriber.assertNoErrors();
        pSubscriber.awaitTerminalEvent();

        System.out.println("Here are all positions at " + theRestaurant.getName() + ":");

        List<PositionModel> positions = pSubscriber.getOnNextEvents().get(0);

        Assert.assertFalse("No positions found related to " + theRestaurant.getName(), positions.isEmpty());

        positions.forEach(p -> System.out.println(p.getEmployeeName() + ": " + p.getPositionName()));
    }
}
