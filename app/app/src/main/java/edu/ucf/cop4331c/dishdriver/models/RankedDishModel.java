package edu.ucf.cop4331c.dishdriver.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by copper on 4/1/17.
 */

public class RankedDishModel extends DishModel {

    // region Field Definition
    private int timesOrdered;
    private int profitEarned;
    // endregion

    public RankedDishModel(DishModel d) {
        setID(d.getID());
        setName(d.getName());
        setDescription(d.getDescription());
        setPrice(d.getPrice());
    }

    public static Observable<List<RankedDishModel>> between(RestaurantModel r, Date start, Date end) {
        HashMap<Integer, RankedDishModel> found = new HashMap<>();

        return OrderedDishModel.between(r, start, end)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(dishes -> {

            dishes.forEach(d -> {
                if (!found.containsKey(d.getDishId())) found.put(d.getDishId(), new RankedDishModel(d));
                found.get(d.getDishId()).countOrder(d);
            });

            List<RankedDishModel> rds = new ArrayList<>(found.values());
            Collections.sort( rds, (rd1, rd2) -> rd2.profitEarned - rd1.profitEarned );

            return Observable.just(rds);
        });
    }

    // region Getters and Setters
    public int getTimesOrdered() { return timesOrdered; }

    public void setTimesOrdered(int timesOrdered) { this.timesOrdered = timesOrdered; }

    public int getProfitEarned() { return profitEarned; }

    public void setProfitEarned(int profitEarned) { this.profitEarned = profitEarned; }

    public void countOrder(OrderedDishModel od) {
        ++timesOrdered;
        profitEarned += od.getOrderedPrice();
    }
    // endregion
}
