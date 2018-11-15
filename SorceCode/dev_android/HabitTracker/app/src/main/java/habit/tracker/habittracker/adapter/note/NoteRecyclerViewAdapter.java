package habit.tracker.habittracker.adapter.note;

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

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.NoteViewHolder> {
    Context context;
    LayoutInflater mInflater;
    List<NoteItem> data;
    RecyclerViewItemClickListener mListener;

    public NoteRecyclerViewAdapter(Context context, List<NoteItem> data, RecyclerViewItemClickListener mListener) {
        this.context = context;
        this.data = data;
        this.mListener = mListener;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View v = mInflater.inflate(R.layout.item_note, viewGroup, false);
        return new NoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        NoteItem item = data.get(position);
        holder.tvDate.setText(item.getDate());
        holder.tvNote.setText(item.getNote());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvNote)
        TextView tvNote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            NoteRecyclerViewAdapter.this.mListener.onItemClick(v, getAdapterPosition());
        }
    }
}
