package habit.tracker.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showEmptyScreen() {
        Intent intent = new Intent(this, EmptyActivity.class);
        startActivity(intent);
    }
}
