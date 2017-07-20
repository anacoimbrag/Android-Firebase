package me.anacoimbra.androidfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    String name;
    String email;
    String password;
    List<String> interests;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("users");

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        ButterKnife.bind(this);

        setTitle(R.string.register_user_title);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.register_button)
    public void onRegisterClick() {
        if (validate()) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = task.getResult().getUser();
                                User user = new User(firebaseUser.getUid(), name, email, interests);
                                userRef.child(user.getUid()).setValue(user);
                                Intent intent = new Intent(RegisterUserActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }
    }

    private boolean validate() {
        name = nameInput.getText().toString();
        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();
        interests = interestsInput.getTags();

        if (name == null || name.isEmpty()) {
            nameLayout.setError(getString(R.string.required_field_error));
            return false;
        } else nameLayout.setError("");

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
