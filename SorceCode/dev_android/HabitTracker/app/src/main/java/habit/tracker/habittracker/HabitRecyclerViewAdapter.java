package habit.tracker.habittracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class HabitRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private boolean isEditable = true;
    public static final int TYPE_CHECK = 0;
    public static final int TYPE_COUNT = 1;
    public static final int TYPE_ADD = 2;
    private Context context;

    private List<TrackingItem> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public HabitRecyclerViewAdapter(Context context, List<TrackingItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    @Override
    public int getItemCount() {
        return  mData == null? 0: mData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mData.size()) {
            return TYPE_ADD;
        } else if (TYPE_COUNT == mData.get(position).getType()) {
            return TYPE_COUNT;
        } else if (TYPE_CHECK == mData.get(position).getType()) {
            return TYPE_CHECK;
        }
        return TYPE_COUNT;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_ADD == viewType) {
            View view = mInflater.inflate(R.layout.item_habit_add, parent, false);
            return new ViewHolderAdd(view);
        } else if (TYPE_COUNT == viewType) {
            View view = mInflater.inflate(R.layout.item_habit_count, parent, false);
            return new ViewHolderCount(view);
        } else if (TYPE_CHECK == viewType) {
            View view = mInflater.inflate(R.layout.item_habit_check, parent, false);
            return new ViewHolderCheck(view);
        } else {
            throw new RuntimeException("unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_ADD:
                break;
            case TYPE_COUNT:
                initLayoutCount((ViewHolderCount) holder, mData.get(position));
                break;
            case TYPE_CHECK:
                initLayoutCheck((ViewHolderCheck) holder, mData.get(position));
                break;
        }
    }

    private Drawable getBackground(String color) {
        Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.bg_shadow);
        if (mDrawable != null && color != null) {
            mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(color),PorterDuff.Mode.MULTIPLY));
        }
        return mDrawable;
    }

    @SuppressLint("ResourceType")
    private void initLayoutCount(ViewHolderCount holder, TrackingItem item) {
        holder.tvCategory.setText(item.getName());
        holder.tvDescription.setText(item.getDescription());
        holder.tvHabitType.setText(oncvertHabitType(item.getHabitType()));
        holder.tvNumber.setText("/" + item.getNumber() + " " + item.getUnit());
        holder.tvCount.setText(String.valueOf(item.getCount()));
        String color = item.getColor();
        if (color != null && color.equals(context.getString(R.color.color0))) {
            color = context.getString(R.color.gray1);
        }
        holder.layout.setBackground(getBackground(color));
        holder.background.setBackground(getBackground(color));
        float comp = (float) item.getCount() / Integer.parseInt(item.getNumber());
        scaleView(holder.background, item.getComp(), comp > 1 ? 1f : comp);
        item.setComp(comp);
    }

    @SuppressLint("ResourceType")
    private void initLayoutCheck(ViewHolderCheck holder, TrackingItem item) {
        holder.tvCategory.setText(item.getName());
        holder.tvDescription.setText(item.getDescription());
        holder.tvHabitType.setText(oncvertHabitType(item.getHabitType()));
        String color = item.getColor();
        if (color != null && color.equals(context.getString(R.color.color0))) {
            color = context.getString(R.color.gray1);
        }
        holder.layout.setBackground(getBackground(color));
        holder.background.setBackground(getBackground(color));
        if (item.getCount() == 1) {
            holder.isCheck = true;
            holder.imgCheck.setImageResource(R.drawable.ck_checked);
            scaleView(holder.background, 0f, 1f);
        } else if (item.getCount() == 0) {
            holder.isCheck = false;
            holder.imgCheck.setImageResource(R.drawable.ck_unchecked);
            scaleView(holder.background, 1f, 0f);
        }
    }

    public class ViewHolderCount extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout layout;
        View background;
        TextView tvCategory;
        TextView tvDescription;
        TextView tvHabitType;
        TextView tvNumber;
        TextView tvCount;
        View btnPlus;
        View btnMinus;

        ViewHolderCount(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            layout = itemView.findViewById(R.id.rl_habit);
            background = itemView.findViewById(R.id.view_bg);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvHabitType = itemView.findViewById(R.id.tv_habitType);
            tvNumber = itemView.findViewById(R.id.tv_number);
            tvCount = itemView.findViewById(R.id.tv_count);
            btnPlus = itemView.findViewById(R.id.btn_plus);
            btnPlus.setOnClickListener(this);
            btnMinus = itemView.findViewById(R.id.btn_minus);
            btnMinus.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (!isEditable) {
                return;
            }
            if (view.getId() == R.id.btn_plus) {
//                Toast.makeText(context, "btn_plus", Toast.LENGTH_SHORT).show();
                int num = Integer.parseInt(tvCount.getText().toString());
                num = num + 1;
                tvCount.setText(num + "");
                mClickListener.onSetCount(view, TYPE_COUNT, getAdapterPosition(), num);
            } else if (view.getId() == R.id.btn_minus) {
//                Toast.makeText(context, "btn_minus", Toast.LENGTH_SHORT).show();
                int num = Integer.parseInt(tvCount.getText().toString());
                num = num > 0? num - 1: 0;
                tvCount.setText(num + "");
                mClickListener.onSetCount(view, TYPE_COUNT, getAdapterPosition(), num);
            } else if (mClickListener != null) {
                mClickListener.onItemClick(view, TYPE_COUNT, getAdapterPosition());
            }
        }
    }

    public class ViewHolderCheck extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout layout;
        View background;
        TextView tvCategory;
        TextView tvDescription;
        TextView tvHabitType;
        ImageView imgCheck;
        boolean isCheck = false;

        ViewHolderCheck(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            layout = itemView.findViewById(R.id.rl_habit);
            background = itemView.findViewById(R.id.view_bg);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvHabitType = itemView.findViewById(R.id.tv_habitType);
            imgCheck = itemView.findViewById(R.id.ck_check);
            imgCheck.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (!isEditable) {
                return;
            }
            if (view.getId() == R.id.ck_check) {
                if (isCheck) {
                    isCheck = false;
                    imgCheck.setImageResource(R.drawable.ck_unchecked);
                    mClickListener.onSetCount(view, TYPE_CHECK, getAdapterPosition(), 0);
                } else {
                    isCheck = true;
                    imgCheck.setImageResource(R.drawable.ck_checked);
                    mClickListener.onSetCount(view, TYPE_CHECK, getAdapterPosition(), 1);
                }
            } else if (mClickListener != null) {
                mClickListener.onItemClick(view, TYPE_CHECK, getAdapterPosition());
            }
        }
    }

    public void scaleView(View v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                startScale, endScale, // Start and end values for the X axis scaling
                1f, 1f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, v.getMeasuredHeight()); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(500);
        v.startAnimation(anim);
    }

    private String oncvertHabitType(String type) {
        switch (type) {
            case "0":
                return "hôm nay";
            case "1":
                return "tuần này";
            case "2":
                return "tháng này";
            case "3":
                return "năm nay";
            default:
                return "";
        }
    }

    public class ViewHolderAdd extends RecyclerView.ViewHolder implements View.OnClickListener {

        ViewHolderAdd(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, TYPE_ADD, getAdapterPosition());
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onSetCount(View view, int type, int position, int count);
        void onItemClick(View view, int type, int position);
    }
}
