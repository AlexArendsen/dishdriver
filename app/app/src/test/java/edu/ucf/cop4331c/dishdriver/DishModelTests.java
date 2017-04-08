package edu.ucf.cop4331c.dishdriver;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import edu.ucf.cop4331c.dishdriver.models.DishModel;
import edu.ucf.cop4331c.dishdriver.models.NonQueryResponseModel;
import edu.ucf.cop4331c.dishdriver.models.RestaurantModel;
import rx.Observable;
import rx.observers.TestSubscriber;

public class DishModelTests {

    @Test
    public void GetDishesFromChadThai() throws Exception {
        Observable<List<RestaurantModel>> oChad = RestaurantModel.search("Chad");
        TestSubscriber<List<RestaurantModel>> sChad = new TestSubscriber<>();
        oChad.subscribe(sChad);

        sChad.assertNoErrors();
        sChad.awaitTerminalEvent();

        RestaurantModel chad = sChad.getOnNextEvents().get(0).get(0);

        System.out.println("This is the menu for " + chad.getName());
        System.out.println("----");

        Observable<List<DishModel>> oDishes = DishModel.forRestaurant(chad);
        TestSubscriber<List<DishModel>> sDishes = new TestSubscriber<>();
        oDishes.subscribe(sDishes);

        sDishes.assertNoErrors();
        sDishes.awaitTerminalEvent();

        List<DishModel> dishes = sDishes.getOnNextEvents().get(0);

        dishes.stream().forEach(d -> System.out.println(d.getName() + ": " + d.getPrice() + " -- " + d.getDescription()));

    }

    @Test
    public void AllTestsHaveTheSameRestaurant() {
        final int rID = 3;

        Observable<List<RestaurantModel>> oChad = RestaurantModel.get(rID);
        TestSubscriber<List<RestaurantModel>> sChad = new TestSubscriber<>();
        oChad.subscribe(sChad);

        sChad.assertNoErrors();
        sChad.awaitTerminalEvent();

        RestaurantModel chad = sChad.getOnNextEvents().get(0).get(0);

        Observable<List<DishModel>> oDishes = DishModel.forRestaurant(chad);
        TestSubscriber<List<DishModel>> sDishes = new TestSubscriber<>();
        oDishes.subscribe(sDishes);

        sDishes.assertNoErrors();
        sDishes.awaitTerminalEvent();

        List<DishModel> dishes = sDishes.getOnNextEvents().get(0);

        // Now that all that shit's done, make sure that all of the dishes have the same restaurant ID.
        Assert.assertTrue(dishes.stream().allMatch(d -> d.getRestaurantID() == rID));

    }

    @Test
    public void CreateADishAndThenDeleteIt() {
        final String dName = "MY FUN DISH";
        final String dDescription = "MY FUN DESCRIPTION";
        final int dPrice = 9999;

        DishModel d = new DishModel();
        d.setName(dName);
        d.setDescription(dDescription);
        d.setPrice(dPrice);
        d.setRestaurantID(3);

        Observable<NonQueryResponseModel> oCreate = d.create();
        TestSubscriber<NonQueryResponseModel> sCreate = new TestSubscriber<>();
        oCreate.subscribe(sCreate);

        sCreate.assertNoErrors();
        sCreate.awaitTerminalEvent();

        Observable<List<RestaurantModel>> oChad = RestaurantModel.get(3);
        TestSubscriber<List<RestaurantModel>> sChad = new TestSubscriber<>();
        oChad.subscribe(sChad);

        sChad.assertNoErrors();
        sChad.awaitTerminalEvent();

        RestaurantModel chad = sChad.getOnNextEvents().get(0).get(0);

        Observable<List<DishModel>> oDishes = DishModel.forRestaurant(chad);
        TestSubscriber<List<DishModel>> sDishes = new TestSubscriber<>();
        oDishes.subscribe(sDishes);

        sDishes.assertNoErrors();
        sDishes.awaitTerminalEvent();

        List<DishModel> dishes = sDishes.getOnNextEvents().get(0);

        // Check that our
        DishModel found = dishes.stream().filter(x -> {
            return x.getName().equals(dName) && x.getDescription().equals(dDescription) && (x.getPrice() == dPrice);
        }).findFirst().get();

        System.out.println("Found from the db! " + found.getName());

        Assert.assertNotNull(found);

        // TODO -- Check if deletion works
        found.delete();

    }

}