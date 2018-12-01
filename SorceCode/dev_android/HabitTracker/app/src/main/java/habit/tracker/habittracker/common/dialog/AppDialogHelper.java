package habit.tracker.habittracker.common.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;

import habit.tracker.habittracker.HabitActivity;
import habit.tracker.habittracker.R;
import habit.tracker.habittracker.common.habitreminder.HabitReminderManager;

public class AppDialogHelper {

    DialogInterface.OnClickListener positiveListener;
    DialogInterface.OnClickListener negativeListener;

    public void setPositiveListener(DialogInterface.OnClickListener positiveListener) {
        this.positiveListener = positiveListener;
    }

    public void setNegativeListener(DialogInterface.OnClickListener negativeListener) {
        this.negativeListener = negativeListener;
    }

    @SuppressLint("ResourceType")
    public AlertDialog getDialog(final Context context, String message, String positiveText, String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (positiveListener != null) {
                    positiveListener.onClick(dialog, id);
                }
                dialog.cancel();
            }
        });

        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (negativeListener != null) {
                    negativeListener.onClick(dialog, id);
                }
                dialog.cancel();
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor(context.getString(R.color.colorAccent)));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor(context.getString(R.color.colorAccent)));
            }
        });
        return alertDialog;
    }
}
