package edu.ucf.cop4331c.dishdriver;

import java.util.ArrayList;

import edu.ucf.cop4331c.dishdriver.models.OrderModel;
import edu.ucf.cop4331c.dishdriver.models.OrderedDishModel;

public class Order {
    OrderModel mOrderModel;
    ArrayList<OrderedDishModel> mOrderedDishModels;

    public Order(OrderModel mOrderModel, ArrayList<OrderedDishModel> mOrderedDishModels) {
        this.mOrderModel = mOrderModel;
        this.mOrderedDishModels = mOrderedDishModels;
    }

    public OrderModel getmOrderModel() {
        return mOrderModel;
    }

    public void setmOrderModel(OrderModel mOrderModel) {
        this.mOrderModel = mOrderModel;
    }

    public ArrayList<OrderedDishModel> getmOrderedDishModels() {
        return mOrderedDishModels;
    }

    public void setmOrderedDishModels(ArrayList<OrderedDishModel> mOrderedDishModels) {
        this.mOrderedDishModels = mOrderedDishModels;
    }
}
