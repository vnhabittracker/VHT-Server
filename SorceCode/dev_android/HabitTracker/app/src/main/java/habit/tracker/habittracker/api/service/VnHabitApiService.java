package habit.tracker.habittracker.api.service;

import habit.tracker.habittracker.api.model.feedback.Feedback;
import habit.tracker.habittracker.api.model.feedback.FeedbackResponse;
import habit.tracker.habittracker.api.model.group.Group;
import habit.tracker.habittracker.api.model.group.GroupResponse;
import habit.tracker.habittracker.api.model.habit.Habit;
import habit.tracker.habittracker.api.model.habit.HabitResponse;
import habit.tracker.habittracker.api.model.reminder.Reminder;
import habit.tracker.habittracker.api.model.search.SearchResponse;
import habit.tracker.habittracker.api.model.suggestion.HabitSuggestion;
import habit.tracker.habittracker.api.model.suggestion.SuggestByLevelReponse;
import habit.tracker.habittracker.api.model.tracking.TrackingList;
import habit.tracker.habittracker.api.model.user.UpdateScoreRequest;
import habit.tracker.habittracker.api.model.user.User;
import habit.tracker.habittracker.api.model.user.UserResponse;
import habit.tracker.habittracker.api.model.user.UserResult;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface VnHabitApiService {
    @FormUrlEncoded
    @POST("user/read_single.php")
    Call<UserResponse> getUser(@Field("username") String username, @Field("password") String password);

    @POST("user/create.php")
    Call<UserResult> registerUser(@Body User user);

    @POST("user/update.php")
    Call<ResponseBody> updateUser(@Body User user);

    @POST("user/social_login.php")
    Call<UserResponse> registerSocialLogin(@Body User user);

    @POST("habit/create.php")
    Call<ResponseBody> addHabit(@Body Habit habit);

    @POST("habit/update.php")
    Call<ResponseBody> updateHabit(@Body Habit habit);

    @DELETE("habit/delete.php")
    Call<ResponseBody> deleteHabit(@Query("habit_id") String habitId);

    @GET("habit/read_by_user.php")
    Call<HabitResponse> getHabit(@Query("user_id") String userId);

    @GET("group/read.php")
    Call<GroupResponse> getGroups(@Query("user_id") String userId);

    @POST("group/create.php")
    Call<ResponseBody> addNewGroup(@Body Group group);

    @GET("group/delete.php")
    Call<GroupResponse> deleteGroup(@Query("group_id") String groupId);

    @POST("tracking/create_update.php")
    Call<ResponseBody> saveUpdateTracking(@Body TrackingList trackingList);

    @GET("suggest/habit_search.php")
    Call<SearchResponse> searchHabitName(@Query("search") String searchKey);

    @GET("suggest/read.php")
    Call<SearchResponse> getAllHabitSuggestion();

    @POST("suggest/update_track_status.php")
    Call<ResponseBody> updateTrackNameStatus(@Body HabitSuggestion habitSuggestion);

    @GET("suggest/get_suggest_by_level.php")
    Call<SuggestByLevelReponse> getHabitSuggestByLevel();

    @POST("user/update_score.php")
    Call<ResponseBody> updateUserScore(@Body UpdateScoreRequest updateScoreRequest);

    @POST("reminder/create_update.php")
    Call<ResponseBody> addUpdateReminder(@Body Reminder reminder);

    @POST("feedback/create_update.php")
    Call<FeedbackResponse> getFeedback(@Query("user_id") String userId);

    @POST("feedback/create_update.php")
    Call<ResponseBody> sendFeedback(@Body Feedback feedback);
}
