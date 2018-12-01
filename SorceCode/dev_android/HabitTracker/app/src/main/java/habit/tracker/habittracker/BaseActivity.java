package habit.tracker.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void editHabitDetails(String habitId) {
        Intent intent = new Intent(this, HabitActivity.class);
        intent.putExtra(MainActivity.HABIT_ID, habitId);
        startActivityForResult(intent, HabitActivity.REQUEST_UPDATE);
    }

    public void showStatics(View view) {
        Intent intent = new Intent(this, StaticsActivity.class);
        startActivity(intent);
        finish();
    }

    public void showProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    public void showNoteScreen(String habitId) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra(MainActivity.HABIT_ID, habitId);
        startActivity(intent);
        finish();
    }

    public void showDetailsChart(String habitId) {
        Intent intent = new Intent(this, ReportDetailsActivity.class);
        intent.putExtra(MainActivity.HABIT_ID, habitId);
        startActivity(intent);
        finish();
    }

    public void showOnCalendar(String habitId) {
        Intent intent = new Intent(this, ReportCalendarActivity.class);
        intent.putExtra(MainActivity.HABIT_ID, habitId);
        startActivity(intent);
        finish();
    }

    public void finishThis(View view) {
        setResult(RESULT_OK);
        finish();
    }

    public void showEmptyScreen() {
        Intent intent = new Intent(this, EmptyActivity.class);
        startActivity(intent);
    }
}
