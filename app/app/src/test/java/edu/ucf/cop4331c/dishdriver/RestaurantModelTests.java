package edu.ucf.cop4331c.dishdriver;

import android.util.Log;

import junit.framework.Assert;

import org.junit.Test;

import java.util.List;

import edu.ucf.cop4331c.dishdriver.models.DishModel;
import edu.ucf.cop4331c.dishdriver.models.RestaurantModel;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * Created by copper on 3/27/17.
 */

public class RestaurantModelTests {
    @Test
    public void GetRestaurantWithIdOne() throws Exception {
        RestaurantModel.get(1).doOnNext(r -> {
            Assert.assertFalse(r.isEmpty());
            Assert.assertNotNull(r.get(0));
            System.out.println("Success! Found " + r.get(0).getName());
        });
    }

    @Test
    public void SearchForChadThai() throws Exception {
        Observable<List<RestaurantModel>> observable = RestaurantModel.search("").observeOn(Schedulers.test());
        TestSubscriber<List<RestaurantModel>> subscriber = new TestSubscriber<>();
        observable.subscribe(subscriber);

        subscriber.assertNoErrors();
        RestaurantModel chad = subscriber.getOnNextEvents().get(0).get(0);

        System.out.println("Here's Chaddy! " + chad.getName());
    }

    @Test
    public void ChadThaiShouldHaveSomeMenuItems() throws Exception {
        Observable<List<RestaurantModel>> observable = RestaurantModel.search("Chad");
        TestSubscriber<List<RestaurantModel>> subscriber = new TestSubscriber<>();
        observable.subscribe(subscriber);

        subscriber.assertNoErrors();
        RestaurantModel chad = subscriber.getOnNextEvents().get(0).get(0);

        System.out.println("Welcome to " + chad.getName() + "! Here is our menu:");

        Observable<List<DishModel>> menuObservable = chad.menu();
        TestSubscriber<List<DishModel>> menuSubscriber = new TestSubscriber<>();
        menuObservable.subscribe(menuSubscriber);

        menuSubscriber.assertNoErrors();
        List<DishModel> menu = menuSubscriber.getOnNextEvents().get(0);

        for(DishModel d : menu) {
            System.out.println("- " + d.getName() + ": " + d.getPrice());
            System.out.println("  " + d.getDescription());
        }
    }
}
