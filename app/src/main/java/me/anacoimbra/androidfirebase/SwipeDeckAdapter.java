package me.anacoimbra.androidfirebase;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by anacoimbra on 20/07/17.
 */

public class SwipeDeckAdapter extends BaseAdapter {

    private List<String> data;

    public SwipeDeckAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if(v == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            // normally use a viewholder
            v = inflater.inflate(R.layout.partial_card, parent, false);
        }
        ((TextView) v.findViewById(R.id.textView2)).setText(data.get(position));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = (String)getItem(position);
                Log.i("MainActivity", item);
            }
        });

        return v;
    }
}
