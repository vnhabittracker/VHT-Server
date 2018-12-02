package habit.tracker.habittracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import habit.tracker.habittracker.api.VnHabitApiUtils;
import habit.tracker.habittracker.api.model.user.User;
import habit.tracker.habittracker.api.service.VnHabitApiService;
import habit.tracker.habittracker.common.util.AppGenerator;
import habit.tracker.habittracker.common.util.MySharedPreference;
import habit.tracker.habittracker.common.validator.Validator;
import habit.tracker.habittracker.common.validator.ValidatorType;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.user.UserEntity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalActivity extends AppCompatActivity {

    @BindView(R.id.editUsername)
    EditText editUsername;
    @BindView(R.id.editRealName)
    EditText editRealName;
    @BindView(R.id.editDay)
    EditText editDay;
    @BindView(R.id.editMonth)
    EditText editMonth;
    @BindView(R.id.editYear)
    EditText editYear;
    @BindView(R.id.radioMale)
    RadioButton radioMale;
    @BindView(R.id.radioFemale)
    RadioButton radioFemale;
    @BindView(R.id.editOldPassword)
    EditText editPassword;
    @BindView(R.id.editNewPassword)
    EditText editPasswordConfirm;
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.editDescription)
    EditText editDescription;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.btnSave)
    Button btnSave;

    private UserEntity userEntity;
    private boolean isMale = true;

    VnHabitApiService mService = VnHabitApiUtils.getApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);

        Database db = Database.getInstance(this);
        db.open();
        userEntity = Database.getUserDb().getUser(MySharedPreference.getUserId(this));
        db.close();

        Calendar ca = Calendar.getInstance();

        if (userEntity != null) {

            if (userEntity.getDateOfBirth() != null) {
                ca.setTime(AppGenerator.getDate(userEntity.getDateOfBirth(), AppGenerator.YMD_SHORT));
                editDay.setText(String.valueOf(ca.get(Calendar.DAY_OF_MONTH)));
                editMonth.setText(String.valueOf(ca.get(Calendar.MONTH) + 1));
                editYear.setText(String.valueOf(ca.get(Calendar.YEAR)));
            }

            if (userEntity.getGender() != null && userEntity.getGender().equals("0")) {
                radioFemale.setChecked(true);
            }

            editUsername.setText(userEntity.getUsername());
            editRealName.setText(userEntity.getRealName());
            editEmail.setText(userEntity.getEmail());
            editDescription.setText(userEntity.getDescription());
        }
    }

    @OnClick({R.id.btnCancel, R.id.btnBack})
    public void cancel(View v) {
        finish();
    }

    @OnClick(R.id.btnSave)
    public void saveInfo(View v) {
        String username = editUsername.getText().toString();
        final String date = editDay.getText().toString();
        String month = editMonth.getText().toString();
        String year = editYear.getText().toString();
        String dob = year + "-" + month + "-" + date;
        String oldPassword = editPassword.getText().toString();
        String newPassword = editPasswordConfirm.getText().toString();
        String realName = editRealName.getText().toString();
        String email = editEmail.getText().toString();
        String description = editDescription.getText().toString();

        Validator validator = new Validator();
        validator.setErrorMsgListener(new Validator.ErrorMsg() {
            @Override
            public void showError(ValidatorType type, String key) {
                switch (type) {
                    case EMPTY:
                        Toast.makeText(PersonalActivity.this, key + " không được rỗng", Toast.LENGTH_SHORT).show();
                        break;
                    case DATE:
                        Toast.makeText(PersonalActivity.this, key + " không hợp lệ", Toast.LENGTH_SHORT).show();
                        break;
                    case PHONE:
                        Toast.makeText(PersonalActivity.this, key + " không đúng", Toast.LENGTH_SHORT).show();
                        break;
                    case EMAIL:
                        Toast.makeText(PersonalActivity.this, key + " không hợp lệ", Toast.LENGTH_SHORT).show();
                        break;
                    case LENGTH:
                        Toast.makeText(PersonalActivity.this, "Chiều dài " + key + " tối thiểu là 8", Toast.LENGTH_SHORT).show();
                        break;
                    case EQUAL:
                        Toast.makeText(PersonalActivity.this, key + " không đúng", Toast.LENGTH_SHORT).show();
                        break;
                    case DIFF:
                        Toast.makeText(PersonalActivity.this, key + " mới trùng mật khẩu cũ", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        if (!validator.checkEmpty("Tên tài khoản", username)
                || !validator.checkEmpty("Ngày", date)
                || !validator.checkEmpty("Tháng", month)
                || !validator.checkEmpty("Năm", year)
                || !validator.checkEmpty("Email", email)
                || !validator.checkEmpty("Mật khẩu", oldPassword)
                || !validator.checkEmpty("Tên", realName)) {
            return;
        }
        if (!validator.checkDate(dob, AppGenerator.YMD_SHORT, "Ngày sinh")) {
            return;
        }
        if (!validator.checkEmail(email)) {
            return;
        }
        if (!validator.checkLength(oldPassword, 8, "Mật khẩu")) {
            return;
        }
        String[] loginInfo = MySharedPreference.getUser(this);
        if (!validator.checkEqual(oldPassword, loginInfo[2], "Mật khẩu cũ")) {
            return;
        }
        if (!TextUtils.isEmpty(newPassword)) {
            if (!validator.checkDiff(oldPassword, newPassword, "Mật khẩu")) {
                return;
            }
        }

        Database db = Database.getInstance(this);
        db.open();

        final User user = new User();
        user.setUserId(MySharedPreference.getUserId(this));
        user.setUsername(username);
        user.setRealName(realName);
        user.setDateOfBirth(dob);
        user.setGender(isMale ? "1" : "0");
        user.setEmail(email);
        user.setPassword(newPassword);
        user.setDescription(description);
        user.setUpdate(true);

        userEntity.setUsername(user.getUsername());
        userEntity.setRealName(user.getRealName());
        userEntity.setDateOfBirth(user.getDateOfBirth());
        userEntity.setGender(user.getGender());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setDescription(user.getDescription());
        userEntity.setUpdate(user.isUpdate());

        Database.getUserDb().saveUser(userEntity);
        db.close();

        MySharedPreference.saveUser(this, user.getUserId(), user.getUsername(), user.getPassword());
        finish();

        mService.updateUser(user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(PersonalActivity.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
                Database db = Database.getInstance(PersonalActivity.this);
                db.open();
                Database.getUserDb().saveUpdate(user.getUserId(), false);
                db.close();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PersonalActivity.this, "Đã xãy ra lỗi", Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick({R.id.radioMale, R.id.radioFemale})
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radioMale:
                if (checked)
                    isMale = true;
                break;
            case R.id.radioFemale:
                if (checked)
                    isMale = false;
                break;
        }
    }
}
