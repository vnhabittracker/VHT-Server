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
import okhttp3.ResponseBody;
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

    private boolean backFromGuild = false;
    private boolean backFromSignUp = false;

    PushDataService pushDataService;
    VnHabitApiService mService = VnHabitApiUtils.getApiService();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // back from sign up screen
        if (requestCode == SIGN_UP) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    edUsername.setText(data.getStringExtra(USERNAME));
                    backFromSignUp = true;
                }
            }
        } else if (requestCode == GUIDE) {
            if (resultCode == RESULT_OK) {
                backFromGuild = true;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        String registerText = getResources().getString(R.string.register_account);
        SpannableString content = new SpannableString(registerText);
        content.setSpan(new UnderlineSpan(), 0, registerText.length(), 0);
        linkRegister.setText(content);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!backFromSignUp && MySharedPreference.getUserId(this) != null) {
            String[] info = MySharedPreference.getUser(this);
            getUser(info[1], info[2], backFromGuild);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (backFromSignUp) {
            backFromSignUp = false;
        }
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

                getUser(username, password, true);

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

    private void getUser(final String username, final String password, final boolean isLogin) {
        mService.getUser(username, password).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.body().getResult().equals(AppConstant.STATUS_OK)) {
                    Database db = new Database(LoginActivity.this);
                    db.open();
                    User user = response.body().getData();

                    UserEntity userEntity = Database.getUserDb().getUser(user.getUserId());
                    if (userEntity.isUpdate()) {
                        callUpdateUserApi(userEntity.toModel());
                    }
                    else {
                        userEntity.setUserId(user.getUserId());
                        userEntity.setUsername(user.getUsername());
                        userEntity.setEmail(user.getEmail());
                        userEntity.setGender(user.getGender());
                        userEntity.setDateOfBirth(user.getDateOfBirth());
                        userEntity.setPassword(user.getPassword());
                        userEntity.setRealName(user.getRealName());
                        userEntity.setDescription(user.getDescription());
                        userEntity.setCreatedDate(user.getCreatedDate());
                        userEntity.setLastLoginTime(user.getLastLoginTime());
                        userEntity.setContinueUsingCount(user.getContinueUsingCount());
                        userEntity.setCurrentContinueUsingCount(user.getCurrentContinueUsingCount());
                        userEntity.setBestContinueUsingCount(user.getBestContinueUsingCount());
                        userEntity.setUserScore(user.getUserScore());
                        Database.getUserDb().saveUser(userEntity);
                    }
                    db.close();

                    if (isLogin) {
                        showMainScreen(user.getUserId(), user.getUsername(), user.getPassword());
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
                    showMainScreen(userEntity.getUserId(), userEntity.getUsername(), userEntity.getPassword());
                } else {
                    Toast.makeText(LoginActivity.this, "Đăng nhập không thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void callUpdateUserApi(final User user) {
        mService.updateUser(user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(LoginActivity.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
                Database db = Database.getInstance(LoginActivity.this);
                db.open();
                Database.getUserDb().saveUpdate(user.getUserId(), false);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Đã xãy ra lỗi", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showMainScreen(String userId, String username, String password) {
        MySharedPreference.saveUser(this, userId, username, password);
        if (MySharedPreference.get(this, MySharedPreference.FIRST_INSTALL) == null) {
            MySharedPreference.save(this, MySharedPreference.FIRST_INSTALL, "1");
            startActivityForResult(new Intent(this, GuideActivity.class), GUIDE);
        } else {
            pushDataService = new PushDataService(this, userId);
            pushDataService.start();
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
