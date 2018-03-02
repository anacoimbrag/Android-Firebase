package me.anacoimbra.androidfirebase.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import me.anacoimbra.androidfirebase.R;
import me.anacoimbra.androidfirebase.model.User;
import me.anacoimbra.androidfirebase.util.JsonUtils;
import me.anacoimbra.androidfirebase.view.activity.LoginActivity;
import me.anacoimbra.androidfirebase.view.adapter.InterestsAdapter;


public class ProfileFragment extends Fragment {

    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.interests)
    RecyclerView interests;

    Unbinder unbinder;
    InterestsAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        getActivity().setTitle(R.string.profile);

        adapter = new InterestsAdapter();

        interests.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        interests.setAdapter(adapter);

        if (user != null) {
            db.collection("users").document(user.getUid())
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                            if (documentSnapshot.exists()) {
                                User user = JsonUtils.map2Object(documentSnapshot.getData(), User.class);
                                name.setText(user.getName());
                                email.setText(user.getEmail());
                                adapter.setItems(user.getInterests());
                                Glide.with(getActivity()).load(user.getPicture()).into(profileImage);
                            }
                        }
                    });
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.logout_button)
    public void onLogoutClick() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
