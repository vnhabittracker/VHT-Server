package habit.tracker.habittracker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import habit.tracker.habittracker.R;
import habit.tracker.habittracker.api.model.reminder.Reminder;
import habit.tracker.habittracker.common.util.DateGenerator;

public class RemindRecyclerViewAdaper extends RecyclerView.Adapter<RemindRecyclerViewAdaper.ReminderViewHolder> {
    List<Reminder> data;
    private LayoutInflater mInflater;
    Context context;

    public RemindRecyclerViewAdaper(Context context, List<Reminder> data){
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = mInflater.inflate(R.layout.item_remind, viewGroup, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder reminder = data.get(position);
        String dspStr = DateGenerator.convertFormat(
                reminder.getReminderTime(),
                DateGenerator.formatYMD,
                DateGenerator.formatDMY) + " " + reminder.getRemindText();

        holder.tvRemindTime.setText(dspStr);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView tvRemindTime;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRemindTime = itemView.findViewById(R.id.tvRemindTime);
        }
    }
}
