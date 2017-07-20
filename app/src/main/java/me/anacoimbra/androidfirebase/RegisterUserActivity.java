package me.anacoimbra.androidfirebase;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import mabbas007.tagsedittext.TagsEditText;

public class RegisterUserActivity extends AppCompatActivity {

    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.name_input)
    TextInputEditText nameInput;
    @BindView(R.id.name_layout)
    TextInputLayout nameLayout;
    @BindView(R.id.email_input)
    TextInputEditText emailInput;
    @BindView(R.id.email_layout)
    TextInputLayout emailLayout;
    @BindView(R.id.password_input)
    TextInputEditText passwordInput;
    @BindView(R.id.password_layout)
    TextInputLayout passwordLayout;
    @BindView(R.id.interests_input)
    TagsEditText interestsInput;
    @BindView(R.id.interests_layout)
    TextInputLayout interestsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        ButterKnife.bind(this);

        setTitle(R.string.register_user_title);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
