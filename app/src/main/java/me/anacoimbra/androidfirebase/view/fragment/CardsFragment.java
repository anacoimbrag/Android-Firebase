package me.anacoimbra.androidfirebase.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.anacoimbra.androidfirebase.R;
import me.anacoimbra.androidfirebase.model.Library;
import me.anacoimbra.androidfirebase.util.JsonUtils;
import me.anacoimbra.androidfirebase.view.activity.AddLibActivity;
import me.anacoimbra.androidfirebase.view.adapter.SwipeDeckAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class CardsFragment extends Fragment {


    @BindView(R.id.swipe_deck)
    SwipeDeck swipeDeck;

    Unbinder unbinder;
    SwipeDeckAdapter adapter;

    FirebaseUser user;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    HashMap<String, Library> libraries = new HashMap<>();

    private FirebaseAnalytics firebaseAnalytics;

    public CardsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
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
                final DocumentReference libsRef = db.collection("libs")
                        .document(adapter.getItem(position).getUid());

                db.runTransaction(new Transaction.Function<Boolean>() {
                    @Override
                    public Boolean apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        transaction.update(libsRef, "users." + user.getUid(), false);
                        return false;
                    }
                });
            }

            @Override
            public void cardSwipedRight(int position) {
                final DocumentReference libsRef = db.collection("libs")
                        .document(adapter.getItem(position).getUid());

                db.runTransaction(new Transaction.Function<Boolean>() {
                    @Override
                    public Boolean apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        transaction.update(libsRef, "users." + user.getUid(), true);
                        return true;
                    }
                });
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
        db.collection("libs")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        for (DocumentSnapshot document : documentSnapshots) {
                            Library library = JsonUtils.map2Object(document.getData(), Library.class);
                            library.setUid(document.getId());
                            libraries.put(document.getId(), library);
                        }

                        adapter.setData(libraries);
                    }
                });
    }
}
