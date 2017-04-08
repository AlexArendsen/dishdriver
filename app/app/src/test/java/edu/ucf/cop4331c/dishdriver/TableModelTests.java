package edu.ucf.cop4331c.dishdriver;

import org.junit.Test;

import java.util.List;

import edu.ucf.cop4331c.dishdriver.models.RestaurantModel;
import edu.ucf.cop4331c.dishdriver.models.TableModel;
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

        tables.stream().forEach(t -> System.out.println(t.getName()));
    }
}
