package me.anacoimbra.androidfirebase;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anacoimbra on 20/07/17.
 */

public class SwipeDeckAdapter extends BaseAdapter {

    private List<Library> data = new ArrayList<>();

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Library getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<Library> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            // normally use a viewholder
            v = inflater.inflate(R.layout.partial_card, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(v);
        final Library library = getItem(position);

        viewHolder.name.setText(library.getName());
        viewHolder.date.setText(library.getDate());
        viewHolder.description.setText(library.getDescription());
        viewHolder.liscence.setText(library.getLiscence());
        viewHolder.minSdk.setText(library.getMin_sdk() + " - " + getAndroidVersionName(library.getMin_sdk()));
        viewHolder.github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(library.getUrl()));
                v.getContext().startActivity(intent);
            }
        });

        return v;
    }

    private String getAndroidVersionName(int code) {
        if (code == Build.VERSION_CODES.CUPCAKE)
            return "Cupcake";
        if (code == Build.VERSION_CODES.DONUT)
            return "Donut";
        if (code == Build.VERSION_CODES.ECLAIR || code == Build.VERSION_CODES.ECLAIR_0_1 || code == Build.VERSION_CODES.ECLAIR_MR1)
            return "Eclair";
        if (code == Build.VERSION_CODES.FROYO)
            return "Foyo";
        if (code == Build.VERSION_CODES.GINGERBREAD || code == Build.VERSION_CODES.GINGERBREAD_MR1)
            return "Gingerbread";
        if (code == Build.VERSION_CODES.HONEYCOMB || code == Build.VERSION_CODES.HONEYCOMB_MR1 || code == Build.VERSION_CODES.HONEYCOMB_MR2)
            return "Honeycomb";
        if (code == Build.VERSION_CODES.ICE_CREAM_SANDWICH || code == Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            return "Ice Cream Sandwich";
        if (code == Build.VERSION_CODES.JELLY_BEAN || code == Build.VERSION_CODES.JELLY_BEAN_MR1 || code == Build.VERSION_CODES.JELLY_BEAN_MR2)
            return "Jelly Bean";
        if (code == Build.VERSION_CODES.KITKAT)
            return "KitKat";
        if (code == Build.VERSION_CODES.LOLLIPOP || code == Build.VERSION_CODES.LOLLIPOP_MR1)
            return "Lollipop";
        if (code == Build.VERSION_CODES.M)
            return "Marshmallow";
        if (code == Build.VERSION_CODES.N || code == Build.VERSION_CODES.N_MR1)
            return "Nougat";

        return "";
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.liscence)
        TextView liscence;
        @BindView(R.id.min_sdk)
        TextView minSdk;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.github)
        Button github;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
