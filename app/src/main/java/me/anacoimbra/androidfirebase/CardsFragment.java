package me.anacoimbra.androidfirebase;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daprlabs.cardstack.SwipeDeck;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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
import java.util.Map;

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

    HashMap<String, Library> libraries;

    public CardsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cards, container, false);
        unbinder = ButterKnife.bind(this, view);

        user = FirebaseAuth.getInstance().getCurrentUser();

        getActivity().setTitle(R.string.cards);

        adapter = new SwipeDeckAdapter();

        swipeDeck.setAdapter(adapter);

        getData();

        swipeDeck.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {

                libsRef.child(adapter.getItem(position).getUid())
                        .child("users")
                        .child(user.getUid())
                        .setValue(false);
            }

            @Override
            public void cardSwipedRight(int position) {

                libsRef.child(adapter.getItem(position).getUid())
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.card_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add)
            startActivityForResult(new Intent(getActivity(), AddLibActivity.class), 10);

        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        libsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Library>> t = new GenericTypeIndicator<HashMap<String, Library>>() {};
                libraries = dataSnapshot.getValue(t);
                if (libraries != null) {
                    Iterator<Map.Entry<String, Library>> iterator = libraries.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, Library> entry = iterator.next();
                        entry.getValue().setUid(entry.getKey());
                        if (entry.getValue().getUsers().containsKey(user.getUid()) &&
                                entry.getValue().getUsers().get(user.getUid())) {
                            iterator.remove();
                        }
                    }
                    adapter.setData(libraries);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
