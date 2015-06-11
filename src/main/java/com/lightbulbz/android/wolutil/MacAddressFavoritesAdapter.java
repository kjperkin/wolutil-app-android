package com.lightbulbz.android.wolutil;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Created by kevin on 6/11/15.
 */
public class MacAddressFavoritesAdapter extends BaseAdapter implements ListAdapter {

    MacAddressFavoritesModel mModel;

    MacAddressFavoritesAdapter(MacAddressFavoritesModel model) {
        mModel = model;
    }

    @Override
    public int getCount() {
        return mModel.getFavoriteCount();
    }

    @Override
    public MacAddressFavoritesModel.Favorite getItem(int position) {
        return mModel.getFavorite(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MacAddressFavoritesModel.Favorite it = getItem(position);
        View targetView;
        if (convertView != null &&
                convertView.findViewById(R.id.text1) != null &&
                convertView.findViewById(R.id.text2) != null) {
            targetView = convertView;
        } else {
            targetView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mac_address_favorite_item, parent, false);
        }

        ((TextView) targetView.findViewById(R.id.text1)).setText(it.name);
        ((TextView) targetView.findViewById(R.id.text2)).setText(it.addr.toString());

        return targetView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
