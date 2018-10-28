package habit.tracker.habittracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideActivity extends AppCompatActivity {

    @BindView(R.id.tvBack)
    TextView back;

    @BindView(R.id.tvNext)
    TextView next;

    @BindView(R.id.background)
    ImageView background;

    int proc = 0;
    int[] images = {R.drawable.guide1, R.drawable.guide2, R.drawable.guide3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        background.setImageResource(R.drawable.guide1);
    }

    @OnClick(R.id.tvBack)
    public void back(View v) {
        proc = proc - 1;
        if (proc < 0) {
            setResult(RESULT_OK);
            finish();
            return;
        }
        background.setImageResource(images[proc]);
    }

    @OnClick(R.id.tvNext)
    public void next(View v) {
        proc = proc + 1;
        if (proc > 2) {
            setResult(RESULT_OK);
            finish();
            return;
        }
        background.setImageResource(images[proc]);
    }
}
