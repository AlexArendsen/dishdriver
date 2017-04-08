package edu.ucf.cop4331c.dishdriver;

import org.junit.Test;

import java.util.Date;
import java.util.List;

import edu.ucf.cop4331c.dishdriver.models.RestaurantModel;
import edu.ucf.cop4331c.dishdriver.models.TableReservationModel;
import rx.Observable;
import rx.observers.TestSubscriber;

/**
 * Created by rebeca (✿ ♥‿♥) on 4/8/2017.
 */

public class TableReservationModelTests {
    @Test
    public void GetAllTableReservations() {
        Observable<List<TableReservationModel>> oReservations = RestaurantModel.get(3)
                .flatMap(list -> {
                    RestaurantModel r = list.get(0);
                    return TableReservationModel.forRestaurant(r);
                });
        TestSubscriber<List<TableReservationModel>> sReservations = new TestSubscriber<>();
        oReservations.subscribe(sReservations);

        sReservations.assertNoErrors();
        sReservations.awaitTerminalEvent();

        List<TableReservationModel> reservations = sReservations.getOnNextEvents().get(0);

        System.out.println("This is all the reservations (active and not) (๑♡3♡๑)");
        System.out.println("----");

        reservations.stream().forEach(tr -> System.out.println(tr.getPartyName() + ": " + tr.getPartySize() + " -- " + tr.getDeposit() + "--" + tr.getdTRequested() + "--" + tr.getdTAccepted()));
    }

    @Test
    public void GetActiveTableReservations() {
        Observable<List<TableReservationModel>> oReservations = RestaurantModel.get(3)
                .flatMap(list -> {
                    RestaurantModel r = list.get(0);
                    return TableReservationModel.forRestaurant(r);
                });
        TestSubscriber<List<TableReservationModel>> sReservations = new TestSubscriber<>();
        oReservations.subscribe(sReservations);

        sReservations.assertNoErrors();
        sReservations.awaitTerminalEvent();

        List<TableReservationModel> reservations = sReservations.getOnNextEvents().get(0);

        System.out.println("This is all the reservations (๑♡3♡๑)");
        System.out.println("----");

        reservations.stream().forEach(tr -> System.out.println(tr.getPartyName() + ": " + tr.getPartySize() + " -- " + tr.getDeposit() + "--" + tr.getdTRequested() + "--" + tr.getdTAccepted()));
    }
}
