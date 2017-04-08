package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import edu.ucf.cop4331c.dishdriver.network.DishDriverProvider;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by rebeca on 3/14/2017.
 */

public class TableReservationModel {

    // region Field Definitions
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
    // endregion

    // region Constructors
    public TableReservationModel(Integer id, Integer tableId, String partyName, Integer partySize, Integer deposit, Date dTRequested, Date dTAccepted) {
        this.id = id;
        this.tableId = tableId;
        this.partyName = partyName;
        this.partySize = partySize;
        this.deposit = deposit;
        this.dTRequested = dTRequested;
        this.dTAccepted = dTAccepted;
    }
    // endregion

    // region query() implementation
    public static Observable<List<TableReservationModel>> query(String sql, String[] args) {

        return DishDriverProvider.getInstance().queryTableReservations(
                DishDriverProvider.DD_HEADER_CLIENT,
                new SqlModel(sql, args)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(qm -> {

                    // If no response was sent, just give back an empty list so things don't
                    // explode in UI
                    if (qm == null || qm.getResults() == null) return Observable.just(new ArrayList<TableReservationModel>());
                    return Observable.just(Arrays.asList(qm.getResults()));

                });
    }
    // endregion

    /**
     *
     * @param restaurant The restaurant we want information for
     * @return A list containing all the table reservations for the given restaurant
     */
    public Observable<List<TableReservationModel>> forRestaurant(RestaurantModel restaurant){
        return query(
                "SELECT TR.* FROM Table_Reservations TR " +
                "JOIN Tables T ON T.Table_Id = TR.Table_ID " +
                "JOIN Restaurants R ON T.Restaurant_ID = R.Id " +
                "WHERE R.Id = ? " +
                "AND TR.Table_ID =?",
                new String[]{Integer.toString(restaurant.getId()), Integer.toString(getTableId())}
        );
    }

    /**
     *
     * @return deletes all reserved tables that haven't been accepted
     */
    public Observable<NonQueryResponseModel> unreserved(){

        return NonQueryResponseModel.run(
                "DELETE FROM Table_Reservations" +
                "WHERE Id = ?" +
                "AND DT_Accepted = NULL",
                new String[] { Integer.toString(getId()) }

        );

    }

    // region Getters and Setters
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
    // endregion
}
