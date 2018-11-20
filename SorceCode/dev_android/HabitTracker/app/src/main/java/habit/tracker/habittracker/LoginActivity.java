package habit.tracker.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import habit.tracker.habittracker.api.VnHabitApiUtils;
import habit.tracker.habittracker.api.model.user.User;
import habit.tracker.habittracker.api.model.user.UserResponse;
import habit.tracker.habittracker.api.service.VnHabitApiService;
import habit.tracker.habittracker.common.AppConstant;
import habit.tracker.habittracker.common.pushservice.PushDataService;
import habit.tracker.habittracker.common.validator.Validator;
import habit.tracker.habittracker.common.validator.ValidatorType;
import habit.tracker.habittracker.common.util.MySharedPreference;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.user.UserEntity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    public static final int GUIDE = 0;

    public static final int SIGN_UP = 1;

    public static final String USERNAME = "username";

    @BindView(R.id.edit_username)
    EditText edUsername;
    @BindView(R.id.edit_password)
    EditText edPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.link_register)
    TextView linkRegister;
    private boolean justSignUp = false;

    PushDataService pushDataService;
    VnHabitApiService mService = VnHabitApiUtils.getApiService();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SIGN_UP) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    edUsername.setText(data.getStringExtra(USERNAME));
                    justSignUp = true;
                }
            }
        } else if (requestCode == GUIDE) {
            if (resultCode == RESULT_OK) {
                justSignUp = false;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!justSignUp && MySharedPreference.getUserId(this) != null) {
            String[] info = MySharedPreference.getUser(this);
            updateUser(info[1], info[2]);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        String registerText = getResources().getString(R.string.register_account);
        SpannableString content = new SpannableString(registerText);
        content.setSpan(new UnderlineSpan(), 0, registerText.length(), 0);
        linkRegister.setText(content);
    }

    @OnClick({R.id.btn_login, R.id.link_register, R.id.btn_fb_login, R.id.btn_google_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String username = edUsername.getText().toString().trim();
                String password = edPassword.getText().toString().trim();
                Validator validator = new Validator();
                validator.setErrorMsgListener(new Validator.ErrorMsg() {
                    @Override
                    public void showError(ValidatorType type, String key) {
                        Toast.makeText(LoginActivity.this, key + " rỗng", Toast.LENGTH_SHORT).show();
                    }
                });
                if (!validator.checkEmpty("Tên tài khoản", username)
                        || !validator.checkEmpty("Mật khẩu", password)) {
                    return;
                }
                updateUser(username, password);
                break;
            case R.id.btn_fb_login:
                showEmptyScreen();
                break;
            case R.id.btn_google_login:
                showEmptyScreen();
                break;
            case R.id.link_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, SIGN_UP);
                break;
        }
    }

    private void updateUser(final String username, final String password) {
        mService.getUser(username, password).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.body().getResult().equals(AppConstant.RES_OK)) {
                    User user = response.body().getData();
                    Database db = new Database(LoginActivity.this);
                    UserEntity userEntity = new UserEntity();
                    db.open();
                    if (user != null) {
                        userEntity.setUserId(user.getUserId());
                        userEntity.setUsername(user.getUsername());
                        userEntity.setEmail(user.getEmail());
                        userEntity.setPhone(user.getPhone());
                        userEntity.setGender(user.getGender());
                        userEntity.setDateOfBirth(user.getDateOfBirth());
                        userEntity.setPassword(user.getPassword());
                        userEntity.setUserIcon(user.getUserIcon());
                        userEntity.setAvatar(user.getAvatar());
                        userEntity.setUserDescription(user.getCreatedDate());
                        userEntity.setCreatedDate(user.getCreatedDate());
                        userEntity.setLastLoginTime(user.getLastLoginTime());
                        userEntity.setContinueUsingCount(user.getContinueUsingCount());
                        userEntity.setCurrentContinueUsingCount(user.getCurrentContinueUsingCount());
                        userEntity.setBestContinueUsingCount(user.getBestContinueUsingCount());
                        userEntity.setUserScore(user.getUserScore());
                        Database.getUserDb().saveUser(userEntity);
                    }
                    db.close();
                    showMainScreen(user.getUserId(), user.getUsername(), user.getPassword());
                } else {
                    Toast.makeText(LoginActivity.this, "Đăng nhập không thành công!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Database db = new Database(LoginActivity.this);
                db.open();
                UserEntity userEntity = Database.userDaoImpl.getUser(username, password);
                db.close();
                if (userEntity.getUserId() != null) {
                    showMainScreen(userEntity.getUserId(), userEntity.getUsername(), userEntity.getPassword());
                } else {
                    Toast.makeText(LoginActivity.this, "Đăng nhập không thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showMainScreen(String userId, String username, String password) {
        if (justSignUp || MySharedPreference.getUserId(this) == null) {
            MySharedPreference.saveUser(this, userId, username, password);
            pushDataService = new PushDataService(this, userId);
            pushDataService.start();
            startActivityForResult(new Intent(this, GuideActivity.class), GUIDE);
        } else {
            MySharedPreference.saveUser(this, userId, username, password);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void showEmpty(View v) {
        Intent i = new Intent(this, EmptyActivity.class);
        startActivity(i);
    }
}
