package habit.tracker.habittracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements MenuRecyclerViewAdapter.ItemClickListener {

    MenuRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        List<MenuItem> data = new ArrayList<>();
        MenuItem item = new MenuItem("PERSONAL", "12", R.drawable.ps);
        data.add(item);
        item = new MenuItem("WORK", "12", R.drawable.work);
        data.add(item);
        item = new MenuItem("MEET", "12", R.drawable.meet);
        data.add(item);
        item = new MenuItem("HOME", "12", R.drawable.home);
        data.add(item);
        item = new MenuItem("PRIVATE", "12", R.drawable.privateic);
        data.add(item);
        item = new MenuItem("ADD NEW", "", R.drawable.add);
        data.add(item);

        RecyclerView recyclerView = findViewById(R.id.rvMenu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MenuRecyclerViewAdapter(this, data);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
//        Toast.makeText(this, "p: " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, EmptyActivity.class);
        startActivity(intent);
    }
}
