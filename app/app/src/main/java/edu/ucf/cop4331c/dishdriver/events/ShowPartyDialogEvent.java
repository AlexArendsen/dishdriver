package edu.ucf.cop4331c.dishdriver.events;

import edu.ucf.cop4331c.dishdriver.models.PositionModel;
import edu.ucf.cop4331c.dishdriver.models.RestaurantModel;

/**
 * Created by viviennedo on 3/14/17.
 */


// I suppose you will have the dialog pop up here and tie the information here?
public class ShowPartyDialogEvent {

    private int mTableId;

    public ShowPartyDialogEvent(int i) {

        mTableId = i;
    }



    public int getTableId() {
        return mTableId;
    }
    public void setTableId(int tableId) {
        this.mTableId = tableId;
    }


}
