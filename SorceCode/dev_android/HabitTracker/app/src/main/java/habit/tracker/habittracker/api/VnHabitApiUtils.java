package habit.tracker.habittracker.api;

import habit.tracker.habittracker.api.remote.RetrofitClient;
import habit.tracker.habittracker.api.service.VnHabitApiService;

public class VnHabitApiUtils {
    //    public static final String BASE_URL = "http://192.168.42.139/php_rest_vnhabit/api/";
    // TungPT: public static final String BASE_URL = "http://192.168.182.2:8080/php_rest_vnhabit/api/";
    public static final String BASE_URL = "http://192.168.1.65/php_rest_vnhabit/api/";

    public static VnHabitApiService getApiService() {
        return RetrofitClient.getClient(BASE_URL).create(VnHabitApiService.class);
    }
}
