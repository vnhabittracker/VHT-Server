package habit.tracker.habittracker.common.pushservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import habit.tracker.habittracker.common.util.MySharedPreference;

public class AppBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            String userId = MySharedPreference.getUserId(context);
            PushDataService pushDataService = new PushDataService(context, userId);
            pushDataService.start();
        }
    }
}
