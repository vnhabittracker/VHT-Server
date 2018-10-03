package habit.tracker.habittracker.api;

import habit.tracker.habittracker.api.remote.RetrofitClient;
import habit.tracker.habittracker.api.service.ApiService;

public class ApiUtils {
    public static final String BASE_URL = "http://192.168.56.1/php_rest_vnhabit/api/";

    public static ApiService getApiService() {
        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
}
