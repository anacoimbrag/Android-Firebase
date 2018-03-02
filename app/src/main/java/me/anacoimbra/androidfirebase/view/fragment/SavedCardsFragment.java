package me.anacoimbra.androidfirebase.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.anacoimbra.androidfirebase.R;
import me.anacoimbra.androidfirebase.model.Library;
import me.anacoimbra.androidfirebase.util.JsonUtils;
import me.anacoimbra.androidfirebase.view.adapter.SavedCardsAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class SavedCardsFragment extends Fragment {

    @BindView(R.id.items)
    RecyclerView items;
    Unbinder unbinder;

    SavedCardsAdapter adapter;
    HashMap<String, Library> libraries = new HashMap<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.collection("libs")
                    .whereEqualTo(FieldPath.of("users", user.getUid()), true)
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

                            adapter.setItems(libraries);
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
}
