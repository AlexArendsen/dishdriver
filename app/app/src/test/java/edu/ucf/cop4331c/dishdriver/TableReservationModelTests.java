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
        Observable<List<RestaurantModel>> oChad = RestaurantModel.search("Chad");
        TestSubscriber<List<RestaurantModel>> sChad = new TestSubscriber<>();
        oChad.subscribe(sChad);

        sChad.assertNoErrors();
        sChad.awaitTerminalEvent();

        RestaurantModel chad = sChad.getOnNextEvents().get(0).get(0);

        System.out.println("This is all the reservations (active and not) for " + chad.getName() + " (๑♡3♡๑)");
        System.out.println("----");

        Observable<List<TableReservationModel>> oReservations = TableReservationModel.forRestaurant(chad);
        TestSubscriber<List<TableReservationModel>> sReservations = new TestSubscriber<>();
        oReservations.subscribe(sReservations);

        sReservations.assertNoErrors();
        sReservations.awaitTerminalEvent();
        List<TableReservationModel> reservations = sReservations.getOnNextEvents().get(0);

        reservations.stream().forEach(tr -> System.out.println(tr.getPartyName() + ": " + tr.getPartySize() + " -- " + tr.getDeposit() + "--" + tr.getdTRequested() + "--" + tr.getdTAccepted()));
    }

    @Test
    public void GetActiveTableReservations() {
        Observable<List<RestaurantModel>> oChad = RestaurantModel.search("Chad");
        TestSubscriber<List<RestaurantModel>> sChad = new TestSubscriber<>();
        oChad.subscribe(sChad);

        sChad.assertNoErrors();
        sChad.awaitTerminalEvent();

        RestaurantModel chad = sChad.getOnNextEvents().get(0).get(0);

        System.out.println("This is all the active reservations for " + chad.getName() + " (๑♡3♡๑)");
        System.out.println("----");

        Observable<List<TableReservationModel>> oReservations = TableReservationModel.activeForRestaurant(chad);
        TestSubscriber<List<TableReservationModel>> sReservations = new TestSubscriber<>();
        oReservations.subscribe(sReservations);

        sReservations.assertNoErrors();
        sReservations.awaitTerminalEvent();
        List<TableReservationModel> reservations = sReservations.getOnNextEvents().get(0);

        reservations.stream().forEach(tr -> System.out.println(tr.getPartyName() + ": " + tr.getPartySize() + " -- " + tr.getDeposit() + "--" + tr.getdTRequested() + "--" + tr.getdTAccepted()));
    }
}
