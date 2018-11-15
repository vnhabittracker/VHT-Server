package habit.tracker.habittracker.api.service;

import habit.tracker.habittracker.api.model.group.Group;
import habit.tracker.habittracker.api.model.group.GroupResponse;
import habit.tracker.habittracker.api.model.habit.Habit;
import habit.tracker.habittracker.api.model.habit.HabitResponse;
import habit.tracker.habittracker.api.model.search.SearchResponse;
import habit.tracker.habittracker.api.model.suggestion.HabitSuggestion;
import habit.tracker.habittracker.api.model.suggestion.SuggestByLevelReponse;
import habit.tracker.habittracker.api.model.tracking.TrackingList;
import habit.tracker.habittracker.api.model.user.User;
import habit.tracker.habittracker.api.model.user.UserResponse;
import habit.tracker.habittracker.api.model.user.UserResult;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @POST("group/create.php")
    Call<GroupResponse> addNewGroup(@Body Group group);

    @POST("tracking/create_update.php")
    Call<ResponseBody> updateTracking(@Body TrackingList trackingList);

    @GET("search/habit_search.php")
    Call<SearchResponse> searchHabitName(@Query("search") String searchKey);

    @GET("search/read.php")
    Call<SearchResponse> getAllHabitSuggestion();

    @POST("search/update_track_status.php")
    Call<ResponseBody> updateTrackNameStatus(@Body HabitSuggestion habitSuggestion);

    @GET("search/get_suggest_by_level.php")
    Call<SuggestByLevelReponse> getHabitSuggestByLevel();
}
