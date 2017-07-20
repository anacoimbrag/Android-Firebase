package me.anacoimbra.androidfirebase;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecoverPasswordActivity extends AppCompatActivity {

    @BindView(R.id.email_input)
    TextInputEditText emailInput;
    @BindView(R.id.email_layout)
    TextInputLayout emailLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);
        ButterKnife.bind(this);

        setTitle(R.string.recover_password_title);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
