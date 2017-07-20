package me.anacoimbra.androidfirebase;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daprlabs.cardstack.SwipeDeck;

import java.util.ArrayList;
import java.util.Arrays;
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

    public CardsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cards, container, false);
        unbinder = ButterKnife.bind(this, view);

        getActivity().setTitle(R.string.cards);

        List<String> list = Arrays.asList("Lib 1","Lib 2", "Lib 3", "Lib 4", "Lib 5");

        swipeDeck.setAdapter(new SwipeDeckAdapter(list));

        swipeDeck.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Toast.makeText(getActivity(), "Left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cardSwipedRight(int position) {
                Toast.makeText(getActivity(), "Right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cardsDepleted() {
                Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
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
