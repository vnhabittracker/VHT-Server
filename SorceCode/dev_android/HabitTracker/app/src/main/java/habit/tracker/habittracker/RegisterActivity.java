package habit.tracker.habittracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import habit.tracker.habittracker.api.ApiUtils;
import habit.tracker.habittracker.api.model.user.User;
import habit.tracker.habittracker.api.model.user.UserResult;
import habit.tracker.habittracker.api.service.ApiService;
import habit.tracker.habittracker.common.Validator;
import habit.tracker.habittracker.common.ValidatorType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRegister;
    View linkLogin;

    EditText edUsername;
    EditText edEmail;
    EditText edPassword;
    EditText edPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
        linkLogin = findViewById(R.id.link_login);
        linkLogin.setOnClickListener(this);

        edUsername = findViewById(R.id.edit_username);
        edEmail = findViewById(R.id.edit_email);
        edPassword = findViewById(R.id.edit_password);
        edPasswordConfirm = findViewById(R.id.edit_conf_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                String username = edUsername.getText().toString();
                String password = edPassword.getText().toString();
                String email = edEmail.getText().toString();
                String passwordConf = edPasswordConfirm.getText().toString();
                User newUser = new User();
                Validator validator = new Validator();
                validator.setErrorMsgListener(new Validator.ErrorMsg() {
                    @Override
                    public void showError(ValidatorType type, String key) {
                        switch (type) {
                            case EMPTY:
                                Toast.makeText(RegisterActivity.this, key + " is empty", Toast.LENGTH_SHORT).show();
                                break;
                            case EMAIL:
                                Toast.makeText(RegisterActivity.this, key + " is not valid", Toast.LENGTH_SHORT).show();
                                break;
                            case SAME:
                                Toast.makeText(RegisterActivity.this, key + " is not math", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                if (!validator.checkEmpty("username", username)
                        || !validator.checkEmpty("password", password)
                        || !validator.checkEmpty("email", email)
                        || !validator.checkEmpty("password confirm", passwordConf)) {
                    return;
                }
                if (!validator.checkEmail(email)) {
                    return;
                }
                if (!validator.checkEqual(password, passwordConf, "password")) {
                    return;
                }
                register(newUser);
                break;
            case R.id.link_login:
                break;
        }
    }

    private void register(User user) {
        ApiService mService = ApiUtils.getApiService();
        mService.addUser(user).enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                if (response.body().getResult().equals("1")) {
                    Toast.makeText(RegisterActivity.this, "Login with your account", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "not ok", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
