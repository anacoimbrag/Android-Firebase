package me.anacoimbra.androidfirebase.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import mabbas007.tagsedittext.TagsEditText;
import me.anacoimbra.androidfirebase.R;
import me.anacoimbra.androidfirebase.model.User;

public class RegisterUserActivity extends AppCompatActivity {

    @BindView(me.anacoimbra.androidfirebase.R.id.profile_image)
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

    Uri profileUri;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("users");

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference imagesRef = storage.getReference().child("images");

    private FirebaseAnalytics firebaseAnalytics;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        ButterKnife.bind(this);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

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
                                final FirebaseUser firebaseUser = task.getResult().getUser();
                                uploadPicture(firebaseUser);
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

    @OnClick(R.id.profile_image)
    public void onSelectImageClick(View view) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            CropImage.startPickImageActivity(RegisterUserActivity.this);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            startCropImageActivity(imageUri);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profileUri = result.getUri();
                profileImage.setImageURI(profileUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setAspectRatio(300, 300)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(this);
    }

    private void uploadPicture(final FirebaseUser firebaseUser) {
        UploadTask uploadTask = imagesRef.child(firebaseUser.getUid()).putFile(profileUri);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful() && task.getResult().getDownloadUrl() != null) {
                    User user = new User(firebaseUser.getUid(), name, email,
                            task.getResult().getDownloadUrl().toString(), interests);
                    userRef.child(user.getUid()).setValue(user.toMap());
                    registerToAnalytics();
                    openMainActivity();
                }
            }
        });
    }

    private void registerToAnalytics() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.UserProperty.SIGN_UP_METHOD, "email and password");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);
    }

    private void openMainActivity() {
        Intent intent = new Intent(RegisterUserActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
