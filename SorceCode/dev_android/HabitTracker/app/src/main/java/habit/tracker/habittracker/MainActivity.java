package habit.tracker.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import habit.tracker.habittracker.api.ApiUtils;
import habit.tracker.habittracker.api.model.user.UserResponse;
import habit.tracker.habittracker.api.service.ApiService;
import habit.tracker.habittracker.common.Validator;
import habit.tracker.habittracker.common.ValidatorType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edUsername;
    EditText edPassword;
    Button btnLogin;
    View linkRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edUsername = findViewById(R.id.edit_username);
        edPassword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.btn_login);
        linkRegister = findViewById(R.id.link_register);

        btnLogin.setOnClickListener(this);
        linkRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String username = edUsername.getText().toString().trim();
                String password = edPassword.getText().toString().trim();
                Validator validator = new Validator();
                validator.setErrorMsgListener(new Validator.ErrorMsg() {
                    @Override
                    public void showError(ValidatorType type, String key) {
                        Toast.makeText(MainActivity.this, key + " is empty", Toast.LENGTH_SHORT).show();
                    }
                });
                if (!validator.checkEmpty("username", username)
                        || !validator.checkEmpty("password", password)) {
                    return;
                }
                login(username, password);
                break;
            case R.id.link_register:
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
                break;
        }
    }

    private void login(String username, String password) {
        ApiService mService = ApiUtils.getApiService();
        mService.getUser(username, password).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.body().getResult().equals("1")) {
                    Toast.makeText(MainActivity.this, "Login ok!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    MainActivity.this.startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Login failed! username or password is not correct!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Login failed! username or password is not correct!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
