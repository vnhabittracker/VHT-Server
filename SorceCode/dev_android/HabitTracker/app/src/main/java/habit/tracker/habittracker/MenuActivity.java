package habit.tracker.habittracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements MenuRecyclerViewAdapter.ItemClickListener {

    MenuRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        List<MenuItem> data = new ArrayList<>();
        MenuItem item = new MenuItem("Personal", "12", R.drawable.ps);
        data.add(item);
        item = new MenuItem("WORK", "12", R.drawable.work);
        data.add(item);
        item = new MenuItem("MEET", "12", R.drawable.meet);
        data.add(item);
        item = new MenuItem("HOME", "12", R.drawable.home);
        data.add(item);
        item = new MenuItem("PRIVATE", "12", R.drawable.privateic);
        data.add(item);
        item = new MenuItem("ADD NEW", "", R.drawable.ps);
        data.add(item);

        RecyclerView recyclerView = findViewById(R.id.rvMenu);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MenuRecyclerViewAdapter(this, data);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
