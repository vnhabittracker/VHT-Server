package habit.tracker.habittracker.api.service;

import habit.tracker.habittracker.api.model.user.User;
import habit.tracker.habittracker.api.model.user.UserResponse;
import habit.tracker.habittracker.api.model.user.UserResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("user/read_single.php")
    Call<UserResponse> getUser(@Query("username") String username, @Query("password") String password);

    @POST("user/create.php")
//    @FormUrlEncoded
    Call<UserResult> addUser(@Body User user);
}
