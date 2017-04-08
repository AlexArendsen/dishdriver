package edu.ucf.cop4331c.dishdriver;

import android.support.design.widget.TabLayout;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.ucf.cop4331c.dishdriver.enums.Status;
import edu.ucf.cop4331c.dishdriver.helpers.MoneyFormatter;
import edu.ucf.cop4331c.dishdriver.models.DishModel;
import edu.ucf.cop4331c.dishdriver.models.NonQueryResponseModel;
import edu.ucf.cop4331c.dishdriver.models.OrderModel;
import edu.ucf.cop4331c.dishdriver.models.OrderedDishModel;
import edu.ucf.cop4331c.dishdriver.models.PositionModel;
import edu.ucf.cop4331c.dishdriver.models.RestaurantModel;
import edu.ucf.cop4331c.dishdriver.models.TableModel;
import rx.Observable;
import rx.observers.TestSubscriber;

/**
 * Created by copper on 4/7/17.
 */

public class OrderModelTests {

    @Test
    public void FullOrderTransitionLifecycleSuccess() {
        OrderModel o = new OrderModel();

        // Get restaurant #3
        Observable<List<RestaurantModel>> oRestaurant = RestaurantModel.get(3);
        TestSubscriber<List<RestaurantModel>> sRestaurant = new TestSubscriber<>();
        oRestaurant.subscribe(sRestaurant);

        sRestaurant.assertNoErrors();
        sRestaurant.awaitTerminalEvent();

        RestaurantModel restaurant = sRestaurant.getOnNextEvents().get(0).get(0);

        // Get the first waiter we find for restaurant #3
        Observable<PositionModel> oWaiter = restaurant.waiters().flatMap(list -> {
            return Observable.just(list.get(0));
        });
        TestSubscriber<PositionModel> sWaiter = new TestSubscriber<>();
        oWaiter.subscribe(sWaiter);

        sWaiter.assertNoErrors();
        sWaiter.awaitTerminalEvent();

        PositionModel waiter = sWaiter.getOnNextEvents().get(0);

        // Get the first table we find at restaurant #3 as well
        Observable<List<TableModel>> oTable = TableModel.forRestaurant(restaurant);
        TestSubscriber<List<TableModel>> sTable = new TestSubscriber<>();
        oTable.subscribe(sTable);

        sTable.assertNoErrors();
        sTable.awaitTerminalEvent();

        TableModel table = sTable.getOnNextEvents().get(0).get(0);

        // Make sure the state is correct before creation
        Assert.assertEquals("Order state incorrect before creation", Status.NEW, o.getStatus());

        // Create the order
        o = createOrder(o, waiter, table);

        // Make sure that the local instance's state is correct after creation
        Assert.assertEquals("Order state incorrect after creation", Status.NEW, o.getStatus());

        // Cancel that order, make a new one
        o = cancelOrder(o);
        Assert.assertEquals("Order state incorrect after cancellation", Status.CANCELLED, o.getStatus());
        o = createOrder(new OrderModel(), waiter, table);

        // Get some dishes to place with the order
        final OrderModel orderRef = o;
        Observable<List<OrderedDishModel>> oMenu = restaurant.menu().flatMap(list -> {
            int total = 0;
            ArrayList<OrderedDishModel> out = new ArrayList<OrderedDishModel>();
            for(DishModel d : list) {
                if (0 == d.getID() % 2) {
                    out.add(new OrderedDishModel(d, orderRef));
                    System.out.println("Adding " + d.getName() + ": " + MoneyFormatter.format(d.getPrice()));
                    total += d.getPrice();
                }
            }

            System.out.println("-- The total price should be " + MoneyFormatter.format(total));

            return Observable.just(out);
        });
        TestSubscriber<List<OrderedDishModel>> sMenu = new TestSubscriber<>();
        oMenu.subscribe(sMenu);

        sMenu.assertNoErrors();
        sMenu.awaitTerminalEvent();

        List<OrderedDishModel> dishes = sMenu.getOnNextEvents().get(0);

        // Place the order
        o = placeOrder(o, dishes);

        // Check local instance state after place
        Assert.assertEquals("Order state incorrect after placement", Status.PLACED, o.getStatus());

        Observable<OrderModel> oOrder = OrderModel.forWaiter(waiter).flatMap(list -> {
            return Observable.just(list.get(0));
        });
        TestSubscriber<OrderModel> sOrder = new TestSubscriber<>();
        oOrder.subscribe(sOrder);

        sOrder.assertNoErrors();
        sOrder.awaitTerminalEvent();

        OrderModel real = sOrder.getOnNextEvents().get(0);

        // Check remote instance state
        Assert.assertEquals("ID mismatch between database and local order instances", real.getId(), o.getId());
        Assert.assertEquals("State mismatch between database and local order instances", real.getStatus(), o.getStatus());

        // Now, reject order from kitchen
        o = rejectOrder(o);

        // Check state after rejection
        Assert.assertEquals("Local instance not transitioned to rejected state after rejection", Status.REJECTED, o.getStatus());

        // We might test if we can kick a dish out from the order here but we'll leave to be
        // covered in the UI

        // Place the order again, include no new dishes this time
        o = placeOrder(o, null);
        Assert.assertEquals("Local instance not returned to placed state after being placed again", Status.PLACED, o.getStatus());

        // Get the cook who will accept this order
        Observable<PositionModel> oCook = restaurant.cooks().flatMap(list -> {
            return Observable.just(list.get(0));
        });
        TestSubscriber<PositionModel> sCook = new TestSubscriber<>();
        oCook.subscribe(sCook);

        sCook.assertNoErrors();
        sCook.awaitTerminalEvent();

        PositionModel cook = sCook.getOnNextEvents().get(0);

        // This time, accept it in the kitchen
        o = acceptOrder(o, cook);

        // Check accepted state
        Assert.assertEquals("Local instance not transitioned to accepted state after acceptance", Status.ACCEPTED, o.getStatus());

        // Cook the order
        o = cookOrder(o);

        // Check cooked state
        Assert.assertEquals("Local instance not transitioned to cooked state after being marked cooked", Status.COOKED, o.getStatus());

        // Check order price
        int total = getTotal(o);
        System.out.println("The real calculated total was " + MoneyFormatter.format(total));

        // And finally, have the order paid / payed / whatever'd for, check the state
        o = payOrder(o);
        Assert.assertEquals("Local instance not transitioned to paid state after being payed", Status.PAID, o.getStatus());
    }

    private OrderModel cancelOrder(OrderModel o) {
        Observable<NonQueryResponseModel> oCancel = o.cancel();
        TestSubscriber<NonQueryResponseModel> sCancel = new TestSubscriber<>();
        oCancel.subscribe(sCancel);

        sCancel.assertNoErrors();
        sCancel.awaitTerminalEvent();

        return o;
    }

    private OrderModel createOrder(OrderModel o, PositionModel waiter, TableModel table) {
        Observable<NonQueryResponseModel> oCreate = o.create(waiter, table);
        TestSubscriber<NonQueryResponseModel> sCreate = new TestSubscriber<>();
        oCreate.subscribe(sCreate);

        sCreate.assertNoErrors();
        sCreate.awaitTerminalEvent();

        return o;
    }

    private OrderModel placeOrder(OrderModel o, List<OrderedDishModel> dishes) {
        Observable<NonQueryResponseModel> oPlace = o.place(dishes);
        TestSubscriber<NonQueryResponseModel> sPlace = new TestSubscriber<>();
        oPlace.subscribe(sPlace);

        sPlace.assertNoErrors();
        sPlace.awaitTerminalEvent();

        return o;
    }

    private OrderModel rejectOrder(OrderModel o) {
        Observable<NonQueryResponseModel> oReject = o.reject();
        TestSubscriber<NonQueryResponseModel> sReject = new TestSubscriber<>();
        oReject.subscribe(sReject);

        sReject.assertNoErrors();
        sReject.awaitTerminalEvent();

        return o;
    }

    private OrderModel cookOrder(OrderModel o) {
        Observable<NonQueryResponseModel> oCook = o.markCooked();
        TestSubscriber<NonQueryResponseModel> sCook = new TestSubscriber<>();
        oCook.subscribe(sCook);

        sCook.assertNoErrors();
        sCook.awaitTerminalEvent();

        return o;
    }

    private OrderModel payOrder(OrderModel o) {
        Observable<NonQueryResponseModel> oPay = o.markPaid();
        TestSubscriber<NonQueryResponseModel> sPay = new TestSubscriber<>();
        oPay.subscribe(sPay);

        sPay.assertNoErrors();
        sPay.awaitTerminalEvent();

        return o;
    }

    private OrderModel acceptOrder(OrderModel o, PositionModel cook) {
        Observable<NonQueryResponseModel> oAccept = o.accept(cook);
        TestSubscriber<NonQueryResponseModel> sAccept = new TestSubscriber<>();
        oAccept.subscribe(sAccept);

        sAccept.assertNoErrors();
        sAccept.awaitTerminalEvent();

        return o;
    }

    private int getTotal(OrderModel o) {
        Observable<Integer> oTotal = o.dishTotal();
        TestSubscriber<Integer> sTotal = new TestSubscriber<>();
        oTotal.subscribe(sTotal);

        sTotal.assertNoErrors();
        sTotal.awaitTerminalEvent();

        return sTotal.getOnNextEvents().get(0);
    }
}
