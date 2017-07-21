package me.anacoimbra.androidfirebase;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daprlabs.cardstack.SwipeDeck;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class CardsFragment extends Fragment {


    @BindView(R.id.swipe_deck)
    SwipeDeck swipeDeck;

    Unbinder unbinder;
    SwipeDeckAdapter adapter;

    FirebaseUser user;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference libsRef = database.getReference("libs");

    List<Library> libraries;

    public CardsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cards, container, false);
        unbinder = ButterKnife.bind(this, view);

        user = FirebaseAuth.getInstance().getCurrentUser();

        getActivity().setTitle(R.string.cards);

        adapter = new SwipeDeckAdapter();
        swipeDeck.setAdapter(adapter);

        libsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Library>> t = new GenericTypeIndicator<List<Library>>() {};
                libraries = dataSnapshot.getValue(t);
                Iterator<Library> libraryIterator = libraries.iterator();
                while (libraryIterator.hasNext()) {
                    Library l = libraryIterator.next();
                    if (l.getUsers().containsKey(user.getUid()) && l.getUsers().get(user.getUid())) {
                        libraryIterator.remove();
                    }
                }
                adapter.setData(libraries);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        swipeDeck.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {

                libsRef.child(String.valueOf(position))
                        .child("users")
                        .child(user.getUid())
                        .setValue(false);
            }

            @Override
            public void cardSwipedRight(int position) {

                libsRef.child(String.valueOf(position))
                        .child("users").
                        child(user.getUid())
                        .setValue(true);
            }

            @Override
            public void cardsDepleted() {

            }

            @Override
            public void cardActionDown() {
            }

            @Override
            public void cardActionUp() {
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
