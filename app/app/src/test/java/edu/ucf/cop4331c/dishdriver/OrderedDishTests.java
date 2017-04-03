package edu.ucf.cop4331c.dishdriver;

import org.junit.Test;

import java.util.Date;
import java.util.List;

import edu.ucf.cop4331c.dishdriver.models.OrderedDishModel;
import edu.ucf.cop4331c.dishdriver.models.RestaurantModel;
import rx.Observable;
import rx.observers.TestSubscriber;

/**
 * Created by copper on 4/3/17.
 */

public class OrderedDishTests {
    @Test
    public void GetAllOrderedDishes() {
        Observable<List<RestaurantModel>> observable = RestaurantModel.search("Chad");
        TestSubscriber<List<RestaurantModel>> subscriber = new TestSubscriber<>();
        observable.subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.awaitTerminalEvent(); // <--- IMPORTANT! Otherwise subscriber won't wait

        List<List<RestaurantModel>> restaurants = subscriber.getOnNextEvents();
        RestaurantModel chad = restaurants.get(0).get(0);

        Observable<List<OrderedDishModel>> oDishes = OrderedDishModel.between(chad, (new Date(1)), (new Date()));
        TestSubscriber<List<OrderedDishModel>> sDishes = new TestSubscriber<>();
        oDishes.subscribe(sDishes);

        sDishes.assertNoErrors();
        sDishes.awaitTerminalEvent();

        List<List<OrderedDishModel>> dishListList = sDishes.getOnNextEvents();
        List<OrderedDishModel> dishes = dishListList.get(0);

        dishes.forEach(d -> System.out.println("- " + d.getName() + ", " + d.getOrderedPrice()));
    }
}

