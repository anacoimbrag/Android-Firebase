package me.anacoimbra.androidfirebase;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

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

    public SavedCardsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_cards, container, false);
        unbinder = ButterKnife.bind(this, view);

        List<String> list = Arrays.asList("Lib 1","Lib 2", "Lib 3", "Lib 4", "Lib 5");
        items.setAdapter(new SavedCardsAdapter(list));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
