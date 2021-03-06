package me.anacoimbra.androidfirebase.view.fragment;


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

import com.daprlabs.cardstack.SwipeDeck;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.anacoimbra.androidfirebase.model.Library;
import me.anacoimbra.androidfirebase.R;
import me.anacoimbra.androidfirebase.model.User;
import me.anacoimbra.androidfirebase.view.adapter.SwipeDeckAdapter;
import me.anacoimbra.androidfirebase.view.activity.AddLibActivity;


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

    HashMap<String, Library> libraries = new HashMap<>();

    private FirebaseAnalytics firebaseAnalytics;

    public CardsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cards, container, false);
        unbinder = ButterKnife.bind(this, view);

        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

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
        if (item.getItemId() == R.id.action_add) {
            sendAnalytics();
            startActivityForResult(new Intent(getActivity(), AddLibActivity.class), 10);
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendAnalytics() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(FirebaseAnalytics.Param.START_DATE, Calendar.getInstance().getTime());
        firebaseAnalytics.logEvent("ADD_LIB", bundle);
    }

    private void getData() {
        libsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot libSnapshot : dataSnapshot.getChildren()) {
                    Library lib = libSnapshot.getValue(Library.class);
                    if (lib != null) {
                        lib.setUid(libSnapshot.getKey());
                        libraries.put(libSnapshot.getKey(), lib);
                    }
                }
                adapter.setData(libraries);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
