package habit.tracker.habittracker.adapter.suggestion;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import habit.tracker.habittracker.R;
import habit.tracker.habittracker.adapter.RecyclerViewItemClickListener;
import habit.tracker.habittracker.api.model.search.HabitSuggestion;

public class SuggestByGroupAdapter extends RecyclerView.Adapter<SuggestByGroupAdapter.HabitSuggestionViewHolder> {
    private Context context;
    private List<HabitSuggestion> data;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewItemClickListener mItemClickListener;

    public SuggestByGroupAdapter(Context context, List<HabitSuggestion> data, RecyclerViewItemClickListener mItemClickListener) {
        this.context = context;
        this.data = data;
        this.mItemClickListener = mItemClickListener;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SuggestByGroupAdapter.HabitSuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View v = mLayoutInflater.inflate(R.layout.item_suggest_by_group, viewGroup, false);
        return new HabitSuggestionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestByGroupAdapter.HabitSuggestionViewHolder viewHolder, int position) {
        HabitSuggestion item = data.get(position);
        if (data.get(position).isHeader()) {
            viewHolder.tvGroup.setTextSize(12);
            viewHolder.tvGroup.setTypeface(Typeface.DEFAULT_BOLD);
            viewHolder.tvGroup.setPadding(0, 20, 0, 0);
            viewHolder.tvGroup.setText(item.getGroup());
            viewHolder.tvHabitNameCount.setVisibility(View.GONE);
            viewHolder.imgUser.setVisibility(View.GONE);
        } else {
            viewHolder.tvGroup.setTextSize(12);
            viewHolder.tvGroup.setPadding(20, 7, 0, 0);
            viewHolder.tvGroup.setText(item.getHabitNameUni());
            viewHolder.tvHabitNameCount.setText(item.getHabitNameCount());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class HabitSuggestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tvGroup)
        TextView tvGroup;
        @BindView(R.id.tvHabitNameCount)
        TextView tvHabitNameCount;
        @BindView(R.id.imgUser)
        ImageView imgUser;

        public HabitSuggestionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
