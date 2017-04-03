package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.ucf.cop4331c.dishdriver.network.DishDriverProvider;
import rx.Observable;
import rx.schedulers.Schedulers;

public class DishModel {

    // region Field Definitions
    @SerializedName("ID")
    @Expose
    private Integer id;

    @SerializedName("Restaurant_ID")
    @Expose
    private Integer restaurantID;

    @SerializedName("Price")
    @Expose
    private Integer price;

    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("Description")
    @Expose
    private String description;

    @SerializedName("DT_Deleted")
    @Expose
    private Object dTDeleted;
    // endregion

    // region query() Definition
    public static Observable<List<DishModel>> query(String sql, String[] args) {

        return DishDriverProvider.getInstance().queryDishes(
                DishDriverProvider.DD_HEADER_CLIENT,
                new SqlModel(sql, args)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(qm -> {

                    // If no response was sent, just give back an empty list so things don't
                    // explode in UI
                    if (qm == null || qm.getResults() == null) return Observable.just(new ArrayList<DishModel>());
                    return Observable.just(Arrays.asList(qm.getResults()));

                });
    }
    // endregion

    // region DB Retrieval
    public static Observable<List<DishModel>> forRestaurant(RestaurantModel r) {
        return query(
                "SELECT * FROM Dishes WHERE Restaurant_ID = ?",
                new String[] { Integer.toString(r.getId()) }
        );
    }

    public static Observable<List<DishModel>> search(RestaurantModel r, String query) {

        query += "%";

        return query(
                "SELECT * FROM Dishes WHERE Restaurant_ID = ? AND Name LIKE ?",
                new String[] { Integer.toString(r.getId()), query }
        );
    }
    // endregion

    // region DB Modifications
    public Observable<NonQueryResponseModel> create() {
        return NonQueryResponseModel.run(
                "INSERT INTO Dishes (Restaurant_ID, Price, Name, Description) VALUES (?, ?, ?, ?)",
                new String[] {
                        Integer.toString(this.restaurantID),
                        Integer.toString(this.price),
                        this.name,
                        this.description
                }
        );
    }

    public Observable<NonQueryResponseModel> update() {
        return NonQueryResponseModel.run(
                "UPDATE Dishes SET Price = ?, Name = ?, Description = ? WHERE Id = ?",
                new String[] {
                        Integer.toString(this.price),
                        this.name,
                        this.description,
                        Integer.toString(this.id)
                }
        );
    }

    public Observable<NonQueryResponseModel> delete() {
        return NonQueryResponseModel.run(
                "DELETE FROM Dishes WHERE Id = ?",
                new String[] { Integer.toString(this.id) }
        );
    }
    // endregion

    // region Getters and Setters
    public Integer getID() { return id; }

    public void setID(Integer iD) { this.id = iD; }

    public Integer getRestaurantID() { return restaurantID; }

    public void setRestaurantID(Integer restaurantID) { this.restaurantID = restaurantID; }

    public Integer getPrice() { return price; }
    
    public Double getDollarPrice() { return (this.price.doubleValue())/100;}

    public void setPrice(Integer price) { this.price = price; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Object getDTDeleted() { return dTDeleted; }

    public void setDTDeleted(Object dTDeleted) { this.dTDeleted = dTDeleted; }

    // endregion

}
