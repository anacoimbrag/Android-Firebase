package me.anacoimbra.androidfirebase;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class SavedCardsFragment extends Fragment {

    @BindView(R.id.items)
    RecyclerView items;
    Unbinder unbinder;

    SavedCardsAdapter adapter;
    HashMap<String, Library> libraries;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference libsRef = database.getReference("libs");

    public SavedCardsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_cards, container, false);
        unbinder = ButterKnife.bind(this, view);

        getActivity().setTitle(R.string.saved_cards);

        adapter = new SavedCardsAdapter();
        items.setAdapter(adapter);

        libsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Library>> t = new GenericTypeIndicator<HashMap<String, Library>>(){};
                libraries = dataSnapshot.getValue(t);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    if (libraries != null) {
                        Iterator<Map.Entry<String, Library>> iterator = libraries.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<String, Library> entry = iterator.next();
                            entry.getValue().setUid(entry.getKey());
                            if (!entry.getValue().getUsers().containsKey(user.getUid()) ||
                                    !entry.getValue().getUsers().get(user.getUid())) {
                                iterator.remove();
                            }
                        }
                        adapter.setItems(libraries);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
