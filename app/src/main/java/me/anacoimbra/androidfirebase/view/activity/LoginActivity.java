package me.anacoimbra.androidfirebase.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.anacoimbra.androidfirebase.R;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.email_input)
    TextInputEditText emailInput;
    @BindView(R.id.email_layout)
    TextInputLayout emailLayout;
    @BindView(R.id.password_input)
    TextInputEditText passwordInput;
    @BindView(R.id.password_layout)
    TextInputLayout passwordLayout;

    String email;
    String password;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

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
        if (validate()) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Snackbar.make(emailInput, task.getException().getMessage(),
                                        Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });

        }
    }

    private boolean validate() {
        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();

        if (email == null || email.isEmpty()) {
            emailLayout.setError(getString(R.string.required_field_error));
            return false;
        } else emailLayout.setError("");

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError(getString(R.string.invalid_email_error));
            return false;
        } else emailLayout.setError("");

        if (password == null || password.isEmpty()) {
            passwordLayout.setError(getString(R.string.required_field_error));
            return false;
        } else passwordLayout.setError("");

        if (password.length() < 6) {
            passwordLayout.setError(getString(R.string.weak_password_error));
            return false;
        } else passwordLayout.setError("");

        return true;
    }
}
