package habit.tracker.habittracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.ViewHolder> {
    private List<MenuItem> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public MenuRecyclerViewAdapter(Context context, List<MenuItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem item = mData.get(position);
        holder.tvCatagory.setText(item.getName());
        holder.tvNumber.setText(item.getAmount() + " tasks");
        holder.imgTask.setImageResource(item.getMenuIc());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvCatagory;
        TextView tvNumber;
        ImageView imgTask;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvCatagory = itemView.findViewById(R.id.tv_catagory);
            tvNumber = itemView.findViewById(R.id.tv_number);
            imgTask = itemView.findViewById(R.id.imgTask);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
