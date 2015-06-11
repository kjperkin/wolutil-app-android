package com.lightbulbz.android.wolutil;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link Listener}
 * interface.
 */
public class MacAddressFavoritesFragment extends ListFragment {

    private Listener mListener;
    private MacAddressFavoritesAdapter mFavoritesAdapter;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MacAddressFavoritesFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Listener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFavoritesAdapter = new MacAddressFavoritesAdapter(mListener.getFavoritesModel());
        setListAdapter(mFavoritesAdapter);
        //mFavorites.addFavorite("Kevin's PC", MacAddress.parseMacAddress("6c:f0:49:e7:14:19"));
        notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mFavoritesAdapter = null;
        setListAdapter(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFavoriteSelected(position);
        }
    }

    public void notifyDataSetChanged() {
        if (mFavoritesAdapter != null) {
            mFavoritesAdapter.notifyDataSetChanged();
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface Listener {
        void onFavoriteSelected(int position);
        MacAddressFavoritesModel getFavoritesModel();
    }

}
