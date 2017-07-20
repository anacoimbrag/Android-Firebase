package me.anacoimbra.androidfirebase;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecoverPasswordActivity extends AppCompatActivity {

    @BindView(R.id.email_input)
    TextInputEditText emailInput;
    @BindView(R.id.email_layout)
    TextInputLayout emailLayout;

    String email;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);
        ButterKnife.bind(this);

        setTitle(R.string.recover_password_title);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.send_button)
    public void onSendClick() {
        if (validate()) {
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            new AlertDialog.Builder(RecoverPasswordActivity.this)
                                    .setMessage(R.string.sent_email_success)
                                    .setPositiveButton(android.R.string.ok,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            })
                                    .show();
                        }
                    });
        }
    }

    private boolean validate() {
        email = emailInput.getText().toString();

        if (email.isEmpty()) {
            emailLayout.setError(getString(R.string.required_field_error));
            return false;
        } else emailLayout.setError("");

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError(getString(R.string.invalid_email_error));
            return false;
        } else emailLayout.setError("");

        return true;
    }
}
