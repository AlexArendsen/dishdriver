package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by rebeca on 3/14/2017.
 */

public class TableReservationsModel {
    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("Table_ID")
    @Expose
    private Integer tableId;

    @SerializedName("Party_Name")
    @Expose
    private String partyName;
    @SerializedName("Party_Size")
    @Expose
    private Integer partySize;
    @SerializedName("Deposit")
    @Expose
    private Integer deposit;
    @SerializedName("DT_Requested")
    @Expose
    private Date dTRequested;
    @SerializedName("DT_Accepted")
    @Expose
    private Date dTAccepted;

    public TableReservationsModel(Integer id, Integer tableId, String partyName, Integer partySize, Integer deposit, Date dTRequested, Date dTAccepted) {
        this.id = id;
        this.tableId = tableId;
        this.partyName = partyName;
        this.partySize = partySize;
        this.deposit = deposit;
        this.dTRequested = dTRequested;
        this.dTAccepted = dTAccepted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public Integer getPartySize() {
        return partySize;
    }

    public void setPartySize(Integer partySize) {
        this.partySize = partySize;
    }

    public Integer getDeposit() {
        return deposit;
    }

    public void setDeposit(Integer deposit) {
        this.deposit = deposit;
    }

    public Date getdTRequested() {
        return dTRequested;
    }

    public void setdTRequested(Date dTRequested) {
        this.dTRequested = dTRequested;
    }

    public Date getdTAccepted() {
        return dTAccepted;
    }

    public void setdTAccepted(Date dTAccepted) {
        this.dTAccepted = dTAccepted;
    }
}
