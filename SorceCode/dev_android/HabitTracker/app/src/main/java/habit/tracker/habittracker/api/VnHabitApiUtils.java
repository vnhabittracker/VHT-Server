package habit.tracker.habittracker.api;

import habit.tracker.habittracker.api.remote.RetrofitClient;
import habit.tracker.habittracker.api.service.VnHabitApiService;

public class VnHabitApiUtils {
//        public static final String BASE_URL = "https://rocky-dusk-97160.herokuapp.com/api/";
    private static final String BASE_URL = "http://192.168.56.1/php_rest_vnhabit/api/";

    public static VnHabitApiService getApiService() {
        return RetrofitClient.getClient(BASE_URL).create(VnHabitApiService.class);
    }
}
