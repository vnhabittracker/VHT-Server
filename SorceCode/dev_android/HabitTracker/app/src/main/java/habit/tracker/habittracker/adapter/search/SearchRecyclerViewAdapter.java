package habit.tracker.habittracker.adapter.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import habit.tracker.habittracker.R;
import habit.tracker.habittracker.adapter.RecyclerViewItemClickListener;
import habit.tracker.habittracker.api.model.search.HabitSuggestion;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.HabitSuggestionViewHolder> {
    private Context context;
    private List<HabitSuggestion> data;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewItemClickListener mItemClickListener;

    public SearchRecyclerViewAdapter(Context context, List<HabitSuggestion> data, RecyclerViewItemClickListener mItemClickListener) {
        this.context = context;
        this.data = data;
        this.mItemClickListener = mItemClickListener;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HabitSuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View v = mLayoutInflater.inflate(R.layout.item_habit_sugggestion, viewGroup, false);
        return new HabitSuggestionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitSuggestionViewHolder viewHolder, int position) {
        HabitSuggestion suggestion = data.get(position);
        viewHolder.tvHabitName.setText(suggestion.getHabitNameUni());
        viewHolder.tvHabitNameCount.setText(suggestion.getHabitNameCount());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class HabitSuggestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tvHabitName)
        TextView tvHabitName;
        @BindView(R.id.tvHabitNameCount)
        TextView tvHabitNameCount;

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
