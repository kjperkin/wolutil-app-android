package com.lightbulbz.android.wolutil;

import android.app.Activity;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link Listener}
 * interface.
 */
public class MacAddressFavoritesFragment extends ListFragment implements AbsListView.MultiChoiceModeListener, AdapterView.OnItemLongClickListener {

    private Listener mListener;
    private MacAddressFavoritesAdapter mFavoritesAdapter;
    private ActionMode mActionMode;


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

        getListView().setMultiChoiceModeListener(this);
        getListView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
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

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        if (!isDetached() && isResumed()) {
            getActivity().getMenuInflater().inflate(R.menu.context, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        SparseBooleanArray checked = getListView().getCheckedItemPositions();
        if (item.getItemId() == R.id.context_delete) {
            int itemCount = getListAdapter().getCount();
            List<Integer> positions = new ArrayList<>();
            for (int idx = 0; idx < itemCount; idx++) {
                if (checked.get(idx)) {
                    positions.add(idx);
                }
            }
            mListener.onRequestDeleteFavorites(positions);
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mActionMode = null;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (mActionMode == null) {
            if (isResumed()) {
                mActionMode = getActivity().startActionMode(this);
                return true;
            }
        }
        return false;
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
        void onRequestDeleteFavorites(Collection<Integer> positions);
    }

}
