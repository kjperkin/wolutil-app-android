package com.lightbulbz.android.wolutil;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lightbulbz.net.MacAddress;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnMacAddressSelectedListener}
 * interface.
 */
public class MacAddressFavoritesFragment extends ListFragment {

    private static final String KEY_FILE_PATH = "MacAddressFragmentFile.mFavoritesFilePath";
    private OnMacAddressSelectedListener mListener;
    private MacAddressFavoritesModel mFavorites;
    private String mFavoritesFilePath;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MacAddressFavoritesFragment() {
    }

    public static MacAddressFavoritesFragment newInstance(String favoritesPath) {
        MacAddressFavoritesFragment f = new MacAddressFavoritesFragment();
        Bundle b = new Bundle();
        b.putString(KEY_FILE_PATH, favoritesPath);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFavoritesFilePath = getArguments().getString(KEY_FILE_PATH);
        } else if (savedInstanceState != null) {
            mFavoritesFilePath = savedInstanceState.getString(KEY_FILE_PATH);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mFavoritesFilePath != null)
        {
            outState.putString(KEY_FILE_PATH, mFavoritesFilePath);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnMacAddressSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMacAddressSelectedListener");
        }

        /*try {
            FileInputStream in = getActivity().openFileInput("favorites.json");
            MacAddressFavoritesStorage.readJsonStream(in);
            in.close();
        } catch (java.io.IOException e) {
            mFavorites = null;
        }*/
        mFavorites = new MacAddressFavoritesModel();
        mFavorites.addFavorite("Kevin's PC", MacAddress.parseMacAddress("6c:f0:49:e7:14:19"));

        if (mFavorites != null) {
            ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mFavorites.getFavoriteNames().toArray());
            setListAdapter(adapter);
        }
    }

    @Override
    public void onDetach() {
        /*try {
            if (mFavorites != null) {
                FileOutputStream out = getActivity().openFileOutput("favorites.json", Context.MODE_PRIVATE);
                MacAddressFavoritesStorage.writeJsonStream(mFavorites, out);
                out.close();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }*/
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFavoriteSelected(mFavorites.getFavorite((String) getListAdapter().getItem(position)));
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
    public interface OnMacAddressSelectedListener {
        void onFavoriteSelected(MacAddressFavoritesModel.Favorite favorite);
    }

}
