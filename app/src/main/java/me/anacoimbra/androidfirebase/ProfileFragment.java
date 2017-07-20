package me.anacoimbra.androidfirebase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


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

        List<String> items = Arrays.asList("Notifications", "RecyclerView", "Banco de dados", "ProgressBar");

        interests.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        interests.setAdapter(new InterestsAdapter(items));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
