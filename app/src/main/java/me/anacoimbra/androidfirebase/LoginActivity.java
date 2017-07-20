package me.anacoimbra.androidfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.email_input)
    TextInputEditText emailInput;
    @BindView(R.id.email_layout)
    TextInputLayout emailLayout;
    @BindView(R.id.password_input)
    TextInputEditText passwordInput;
    @BindView(R.id.password_layout)
    TextInputLayout passwordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.recover_password_button)
    public void onRecoverPasswordClick() {
        startActivity(new Intent(this, RecoverPasswordActivity.class));
    }

    @OnClick(R.id.register_user_button)
    public void onRegisterUserClick() {
        startActivity(new Intent(this, RegisterUserActivity.class));
    }

    @OnClick(R.id.enter_button)
    public void onEnterClick() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
