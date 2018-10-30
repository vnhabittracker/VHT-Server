package habit.tracker.habittracker.api.service;

import java.util.List;

import habit.tracker.habittracker.api.model.group.GroupResponse;
import habit.tracker.habittracker.api.model.habit.Habit;
import habit.tracker.habittracker.api.model.habit.HabitResponse;
import habit.tracker.habittracker.api.model.habit.HabitResult;
import habit.tracker.habittracker.api.model.tracking.Tracking;
import habit.tracker.habittracker.api.model.tracking.TrackingList;
import habit.tracker.habittracker.api.model.tracking.TrackingResult;
import habit.tracker.habittracker.api.model.user.User;
import habit.tracker.habittracker.api.model.user.UserResponse;
import habit.tracker.habittracker.api.model.user.UserResult;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface VnHabitApiService {
    @GET("user/read_single.php")
    Call<UserResponse> getUser(@Query("username") String username, @Query("password") String password);

    @POST("user/create.php")
    Call<UserResult> addUser(@Body User user);

    @POST("habit/create.php")
    Call<ResponseBody> addHabit(@Body Habit habit);

    @POST("habit/update.php")
    Call<ResponseBody> updateHabit(@Body Habit habit);

    @DELETE("habit/delete.php")
    Call<ResponseBody> deleteHabit(@Query("habit_id") String habitId);

    @GET("habit/read_by_user.php")
    Call<HabitResponse> getHabit(@Query("user_id") String userId);

    @GET("group/read.php")
    Call<GroupResponse> getGroupItems();

    @POST("tracking/create_update.php")
    Call<ResponseBody> replace(@Body TrackingList trackingList);
}
