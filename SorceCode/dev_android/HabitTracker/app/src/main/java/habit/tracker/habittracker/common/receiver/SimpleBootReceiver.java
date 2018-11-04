package habit.tracker.habittracker.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SimpleBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null
                || intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

        }

    }
}
