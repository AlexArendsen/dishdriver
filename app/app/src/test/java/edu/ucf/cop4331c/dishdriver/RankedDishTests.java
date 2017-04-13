package edu.ucf.cop4331c.dishdriver;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import edu.ucf.cop4331c.dishdriver.helpers.MoneyFormatter;
import edu.ucf.cop4331c.dishdriver.models.RankedDishModel;
import edu.ucf.cop4331c.dishdriver.models.RestaurantModel;
import rx.Observable;
import rx.observers.TestSubscriber;

/**
 * Created by copper on 4/1/17.
 */

public class RankedDishTests {

    @Test
    public void GetRankedDishesForStats() throws Exception {

        Observable<List<RestaurantModel>> oChad = RestaurantModel.get(3);
        TestSubscriber<List<RestaurantModel>> sChad = new TestSubscriber<>();
        oChad.subscribe(sChad);

        sChad.assertNoErrors();
        sChad.awaitTerminalEvent();

        RestaurantModel chad = sChad.getOnNextEvents().get(0).get(0);

        Observable<List<RankedDishModel>> oRDs = RankedDishModel.between(chad, new Date(0), new Date());
        TestSubscriber<List<RankedDishModel>> sRDs = new TestSubscriber<>();
        oRDs.subscribe(sRDs);

        sRDs.assertNoErrors();
        sRDs.awaitTerminalEvent();

        System.out.println("Stats for: " + chad.getName());

        List<List<RankedDishModel>> data = sRDs.getOnNextEvents();
        if (data.size() >= 1)
            sRDs.getOnNextEvents().get(0).forEach(rd -> System.out.println(rd.getName() + ": Ordered " + rd.getTimesOrdered() + " times, earning $" + MoneyFormatter.format(rd.getProfitEarned())));
        else
            Assert.fail("No ranked dish lists were provided by the between() method");
    }
}
