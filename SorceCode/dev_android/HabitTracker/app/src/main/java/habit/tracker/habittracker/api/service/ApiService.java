package habit.tracker.habittracker.api.service;

import habit.tracker.habittracker.api.model.user.UserResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("user/read_single.php")
    Call<UserResponse> getUser(@Query("username") String username, @Query("password") String password);
}
