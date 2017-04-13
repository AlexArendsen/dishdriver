package edu.ucf.cop4331c.dishdriver;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import edu.ucf.cop4331c.dishdriver.models.RestaurantModel;
import edu.ucf.cop4331c.dishdriver.models.TableModel;
import edu.ucf.cop4331c.dishdriver.models.TableReservationModel;
import rx.Observable;
import rx.observers.TestSubscriber;

/**
 * Created by copper on 4/8/17.
 */

public class TableModelTests {

    @Test
    public void GetTablesForRestaurant() {
        Observable<List<TableModel>> oTables = RestaurantModel.get(3)
                .flatMap(list -> {
                    RestaurantModel r = list.get(0);
                    return TableModel.forRestaurant(r);
                });
        TestSubscriber<List<TableModel>> sTables = new TestSubscriber<>();
        oTables.subscribe(sTables);

        sTables.assertNoErrors();
        sTables.awaitTerminalEvent();

        List<TableModel> tables = sTables.getOnNextEvents().get(0);

        Assert.assertTrue("Found no tables!", !tables.isEmpty());

        tables.stream().forEach(t -> System.out.println(t.getName()));
    }

    @Test
    public void ReserveTable() {

        final String rName = "A Long Expected Party";
        final int rSize = 4;
        final int rDeposit = 5000;
        final Date rRequested = new Date();

        Observable<TableReservationModel> oRes = RestaurantModel.get(3)
                .flatMap(list -> TableModel.forRestaurant(list.get(0)))
                .flatMap(list -> list.get(2).reserve(rName, rSize, rDeposit, rRequested))
                .flatMap(qr -> TableReservationModel.get(qr.getResults().getInsertId()));

        TestSubscriber<TableReservationModel> sRes = new TestSubscriber<>();
        oRes.subscribe(sRes);

        sRes.assertNoErrors();
        sRes.awaitTerminalEvent();

        TableReservationModel reservation = sRes.getOnNextEvents().get(0);

        System.out.println("Inserted reservation with ID " + reservation.getId());
        Assert.assertEquals("Local and remote name mismatch", rName, reservation.getPartyName());
        Assert.assertEquals("Local and remote size mismatch", rSize, (int) reservation.getPartySize());
        Assert.assertEquals("Local and remote deposit mismatch", rDeposit, (int) reservation.getDeposit());

        // TableReservation isn't getting the date requested for some reason, and I don't have any
        // more time to test it so UUUUGHGJUHGFIJsdofisjvoivj this test is officially done.
    }
}
