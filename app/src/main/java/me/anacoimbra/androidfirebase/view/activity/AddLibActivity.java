package me.anacoimbra.androidfirebase.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.anacoimbra.androidfirebase.model.Library;
import me.anacoimbra.androidfirebase.R;

public class AddLibActivity extends AppCompatActivity {

    @BindView(R.id.name_input)
    TextInputEditText nameInput;
    @BindView(R.id.name_layout)
    TextInputLayout nameLayout;
    @BindView(R.id.date_input)
    MaskedEditText dateInput;
    @BindView(R.id.date_layout)
    TextInputLayout dateLayout;
    @BindView(R.id.license_input)
    TextInputEditText licenseInput;
    @BindView(R.id.license_layout)
    TextInputLayout licenseLayout;
    @BindView(R.id.url_input)
    TextInputEditText urlInput;
    @BindView(R.id.url_layout)
    TextInputLayout urlLayout;
    @BindView(R.id.sdk_input)
    TextInputEditText sdkInput;
    @BindView(R.id.sdk_layout)
    TextInputLayout sdkLayout;
    @BindView(R.id.description_input)
    TextInputEditText descriptionInput;
    @BindView(R.id.description_layout)
    TextInputLayout descriptionLayout;

    String name;
    String date;
    String license;
    String url;
    int sdk;
    String description;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference libsRef = database.getReference("libs");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lib);
        ButterKnife.bind(this);
        setTitle(R.string.add_lib);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.save_button)
    public void onSaveClick() {
        if (validate()) {
            Library library = new Library(name, url, date, sdk, license, description);
            libsRef.push().setValue(library)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                setResult(RESULT_OK);
                                finish();
                            }
                        }
                    });
        }
    }

    private boolean validate() {
        name = nameInput.getText().toString();
        date = dateInput.getText().toString();
        license = licenseInput.getText().toString();
        url = urlInput.getText().toString();
        sdk = Integer.parseInt(sdkInput.getText().toString());
        description = descriptionInput.getText().toString();

        if (name == null || name.isEmpty()) {
            nameLayout.setError(getString(R.string.required_field_error));
            return false;
        } else nameLayout.setError(null);

        if (date == null || date.isEmpty()) {
            dateLayout.setError(getString(R.string.required_field_error));
            return false;
        } else {
            SimpleDateFormat f = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault());
            try {
                Date d = f.parse(date);
                f = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
                date = f.format(d);
                nameLayout.setError(null);
            } catch (ParseException e) {
                FirebaseCrash.log("User entered wrong date format");
                nameLayout.setError(getString(R.string.invalid_date_error));
                return false;
            }
        }

        if (name == null || name.isEmpty()) {
            nameLayout.setError(getString(R.string.required_field_error));
            return false;
        } else nameLayout.setError(null);

        if (name == null || name.isEmpty()) {
            nameLayout.setError(getString(R.string.required_field_error));
            return false;
        } else nameLayout.setError(null);

        if (name == null || name.isEmpty()) {
            nameLayout.setError(getString(R.string.required_field_error));
            return false;
        } else nameLayout.setError(null);

        if (name == null || name.isEmpty()) {
            nameLayout.setError(getString(R.string.required_field_error));
            return false;
        } else nameLayout.setError(null);

        return true;
    }
}
