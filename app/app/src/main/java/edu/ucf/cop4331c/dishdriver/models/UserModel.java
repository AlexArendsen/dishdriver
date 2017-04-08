package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.ucf.cop4331c.dishdriver.network.DishDriverProvider;
import retrofit2.Call;
import rx.Observable;
import rx.schedulers.Schedulers;

import static android.R.id.list;

/**
 * Created by copper on 3/14/17.
 */

public class UserModel {

    // region Field Definitions
    @SerializedName("ID")
    @Expose
    protected Integer id;

    @SerializedName("Email")
    @Expose
    protected String email;

    @SerializedName("Password")
    @Expose
    protected String password;

    @SerializedName("FirstName")
    @Expose
    protected String firstName;

    @SerializedName("LastName")
    @Expose
    protected String lastName;

    @SerializedName("DT_Created")
    @Expose
    protected String dTCreated;

    @SerializedName("SessionToken")
    @Expose
    protected String sessionToken;

    @SerializedName("DT_LastLogin")
    @Expose
    protected String dTLastLogin;

    @SerializedName("DT_Cancelled")
    @Expose
    protected Object dTCancelled;
    // endregion

    // region Constructors

    /**
     * Create a user instance
     *
     * @param email The email
     * @param password The plaintext password
     * @param firstName The user's first name
     * @param lastName The user's last name
     */
    public UserModel(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;//BCrypt.hashpw(password, BCrypt.gensalt());
        this.firstName = firstName;
        this.lastName = lastName;
    }
    // endregion

    // region query() implementation
    public static Observable<List<UserModel>> query(String sql, String[] args) {

        return DishDriverProvider.getInstance().queryUsers(
                DishDriverProvider.DD_HEADER_CLIENT,
                new SqlModel(sql, args)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(qm -> {

                    // If no response was sent, just give back an empty list so things don't
                    // explode in UI
                    if (qm == null || qm.getResults() == null) return Observable.just(new ArrayList<UserModel>());
                    return Observable.just(Arrays.asList(qm.getResults()));

                });
    }
    // endregion

    public Observable<NonQueryResponseModel> create() {
        return NonQueryResponseModel.run(
                "INSERT INTO Users (Email, Password, FirstName, LastName, DT_Created) VALUES (?, ?, ?, ?, NOW())",
                new String[] { email, password, firstName, lastName}
        );
    }

    /**
     * Finds the first user with the provided email address
     *
     * @param email The email of the user to look for
     * @return null if the user is not found, otherwise the UserModel
     */
    public static Observable<UserModel> findFirst(String email) {
        return query(
                "SELECT * FROM Users WHERE Email = ?",
                new String[] { email }
        ).flatMap(list -> {
            return Observable.just((list.isEmpty()) ? null : list.get(0) );
        });
    }

    // region Getters and Setters
    public Integer getID() {
        return id;
    }

    public void setID(Integer iD) {
        this.id = iD;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDTCreated() {
        return dTCreated;
    }

    public void setDTCreated(String dTCreated) {
        this.dTCreated = dTCreated;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getDTLastLogin() {
        return dTLastLogin;
    }

    public void setDTLastLogin(String dTLastLogin) {
        this.dTLastLogin = dTLastLogin;
    }

    public Object getDTCancelled() {
        return dTCancelled;
    }

    public void setDTCancelled(Object dTCancelled) {
        this.dTCancelled = dTCancelled;
    }
    // endregion

    // region Overridden Methods
    public String toString(){
        return "id: "+ this.id +", email: "+ this.email;
    }
    // endregion
}
