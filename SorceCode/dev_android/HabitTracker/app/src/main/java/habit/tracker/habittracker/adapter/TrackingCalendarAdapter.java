package habit.tracker.habittracker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import habit.tracker.habittracker.R;

public class TrackingCalendarAdapter extends RecyclerView.Adapter<TrackingCalendarAdapter.CalendarNumberViewHolder> {

    private LayoutInflater mInflater;
    Context context;
    List<TrackingCalendarItem> data;
    OnItemClickListener clickListener;
    String colorTheme;

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public TrackingCalendarAdapter(Context context, List<TrackingCalendarItem> data, String colorTheme) {
        this.context = context;
        this.data = data;
        this.mInflater = LayoutInflater.from(context);
        this.colorTheme = colorTheme;
    }

    @NonNull
    @Override
    public CalendarNumberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_calendar, viewGroup, false);
        return new CalendarNumberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarNumberViewHolder holder, int pos) {
        if (data.get(pos).isFilled()) {
            holder.tvNumber.setBackground(getBackground(colorTheme));
        } else {
            holder.tvNumber.setBackground(ContextCompat.getDrawable(context, android.R.color.transparent));
        }
        if (data.get(pos).isOutOfRange()) {
            holder.tvNumber.setAlpha(0.4f);
        } else {
            holder.tvNumber.setAlpha(1f);
        }

        holder.tvNumber.setText( data.get(pos).getText() );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected class CalendarNumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNumber;

        public CalendarNumberViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvNumber.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (data.get(this.getAdapterPosition()).isHeader()) {
                return;
            }
            clickListener.onItemClick(v, this.getAdapterPosition());
//            Log.i("calendar_click", "clicked: " + data.get(this.getAdapterPosition()).getText());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private Drawable getBackground(String color) {
        Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.bg_circle_fill);
        if (mDrawable != null && color != null) {
            mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(color), PorterDuff.Mode.MULTIPLY));
        }
        return mDrawable;
    }
}
