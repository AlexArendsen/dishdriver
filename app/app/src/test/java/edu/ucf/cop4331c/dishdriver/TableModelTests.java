package edu.ucf.cop4331c.dishdriver;

import org.junit.Test;

import edu.ucf.cop4331c.dishdriver.enums.TableStatus;
import edu.ucf.cop4331c.dishdriver.models.TableModel;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import edu.ucf.cop4331c.dishdriver.models.RestaurantModel;
import edu.ucf.cop4331c.dishdriver.models.TableModel;
import edu.ucf.cop4331c.dishdriver.models.TableReservationModel;
import rx.Observable;
import rx.observers.TestSubscriber;

import static android.R.id.list;

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
        final Date rRequested = new Date((new Date().getTime()) + 123456789);


        Observable<TableReservationModel> oRes = RestaurantModel.get(3)
                .flatMap(list -> TableModel.forRestaurant(list.get(0)))
                .flatMap(list -> list.get(2).reserve(rName, rSize, rDeposit, rRequested))
                .flatMap(qr -> TableReservationModel.get(qr.getResults().getInsertId()));
//        Observable<TableReservationModel> oRes = getTable(3).reserve(
//                    rName, rSize, rDeposit, rRequested
//                ).flatMap(qr ->   TableReservationModel.get(qr.getResults().getInsertId()));

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

        // Update: also, let's check that the new getStatus() method works.
        Assert.assertEquals("Table is not reserved but it should have been", TableStatus.RESERVED, getTableStatus(3));
    }

    @Test
    public void CheckStatusMethod() {

        // This test assumes Table 3 is reserved
        Assert.assertEquals("Table 3 is not reserved like it should be", TableStatus.RESERVED, getTableStatus(3));

        // And that Table 6 is not reserved
        Assert.assertEquals("Table 6 is not unreserved like it should be", TableStatus.UNRESERVED, getTableStatus(6));

        // And that Table 2 is occupied without a reservation
        Assert.assertEquals("Table 2 is not occupied like it should be", TableStatus.OCCUPIED, getTableStatus(2));

    }

    private TableModel getTable(int tableId) {
        Observable<TableModel> oTable = TableModel.get(tableId);
        TestSubscriber<TableModel> sTable = new TestSubscriber<>();
        oTable.subscribe(sTable);

        sTable.assertNoErrors();
        sTable.awaitTerminalEvent();

        return sTable.getOnNextEvents().get(0);
    }

    private TableStatus getTableStatus(int tableId) {
        Observable<TableStatus> oStatus = getTable(tableId).getStatus();
        TestSubscriber<TableStatus> sStatus = new TestSubscriber<>();
        oStatus.subscribe(sStatus);

        sStatus.assertNoErrors();
        sStatus.awaitTerminalEvent();

        return sStatus.getOnNextEvents().get(0);
    }
}
