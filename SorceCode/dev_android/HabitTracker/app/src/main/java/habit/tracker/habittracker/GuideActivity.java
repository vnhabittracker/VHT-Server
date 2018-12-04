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

    @BindView(R.id.guide_group)
    ImageView imgGuideGroup;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvGuide)
    TextView tvGuide;

    @BindView(R.id.imgDot1)
            ImageView imgDot1;
    @BindView(R.id.imgDot2)
            ImageView imgDot2;
    @BindView(R.id.imgDot3)
            ImageView imgDot3;
    ImageView imgCurrentDot;
    ImageView[] imgDots;

    int process = 0;
    int[] imgModals = {R.drawable.ic_guide1, R.drawable.ic_guide2, R.drawable.ic_guide3};
    String[] titles = {"TẠO THÓI QUEN", "QUẢN LÝ THÓI QUEN", "THEO DÕI LỊCH BIỂU"};
    String[] guides = {
            "Tạo nhanh một thói quen và đề xuất những thói quen phù hợp cho bạn!",
            "Quản lý thói quen và theo dõi theo dòng thời gian thông min!",
            "Bạn có thể lập lịch và xem lịch biểu thói quen một cách dễ dàng!"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        imgCurrentDot = imgDot1;
        imgDots = new ImageView[]{imgDot1, imgDot2, imgDot3};
        imgGuideGroup.setImageResource(R.drawable.ic_guide1);
    }

    @OnClick({R.id.tvNext, R.id.tvBack})
    public void nextAndBack(View v) {
        if (v.getId() == R.id.tvNext) {
            process = process + 1;
            if (process > 2) {
                setResult(RESULT_OK);
                finish();
                return;
            }
        } else if (v.getId() == R.id.tvBack) {
            process = process - 1;
            if (process < 0) {
                setResult(RESULT_OK);
                finish();
                return;
            }
        }
        tvTitle.setText(titles[process]);
        tvGuide.setText(guides[process]);
        imgGuideGroup.setImageResource(imgModals[process]);
        imgCurrentDot.setImageResource(R.drawable.dot_fade);
        imgDots[process].setImageResource(R.drawable.dot_active);
        imgCurrentDot = imgDots[process];
    }
}
