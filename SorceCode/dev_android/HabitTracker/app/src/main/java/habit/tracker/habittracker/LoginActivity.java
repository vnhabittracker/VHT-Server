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
import habit.tracker.habittracker.common.Validator;
import habit.tracker.habittracker.common.ValidatorType;
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

    public static final String PASSWORD = "password";

    @BindView(R.id.edit_username)
    EditText edUsername;
    @BindView(R.id.edit_password)
    EditText edPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.link_register)
    TextView linkRegister;
    private boolean isSignUp = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SIGN_UP) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    edUsername.setText(data.getStringExtra(USERNAME));
                    isSignUp = true;
                }
            }
        } else if (requestCode == GUIDE) {
            if (resultCode == RESULT_OK) {
                isSignUp = false;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isSignUp && MySharedPreference.getUserId(this) != null) {
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
                login(username, password);
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

    private void login(final String username, final String password) {
        VnHabitApiService mService = VnHabitApiUtils.getApiService();
        mService.getUser(username, password).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.body().getResult().equals("1")) {
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
                        userEntity.setUserDescription(user.getUserDescription());
                        Database.userDaoImpl.saveUser(userEntity);
                        db.close();
                        showMainScreen(user.getUserId(), user.getUsername());
                    }
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
                    showMainScreen(userEntity.getUserId(), userEntity.getUsername());
                } else {
                    Toast.makeText(LoginActivity.this, "Đăng nhập không thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showMainScreen(String userId, String username) {
        if (isSignUp || MySharedPreference.getUserId(this) == null) {
            MySharedPreference.saveUser(LoginActivity.this, userId, username);
            startActivityForResult(new Intent(this, GuideActivity.class), GUIDE);
        } else {
            MySharedPreference.saveUser(LoginActivity.this, userId, username);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void showEmpty(View v) {
        Intent i = new Intent(this, EmptyActivity.class);
        startActivity(i);
    }
}
