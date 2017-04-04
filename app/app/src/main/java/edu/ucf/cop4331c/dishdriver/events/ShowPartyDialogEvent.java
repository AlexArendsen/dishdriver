package edu.ucf.cop4331c.dishdriver.events;

/**
 * Created by viviennedo on 3/14/17.
 */

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
