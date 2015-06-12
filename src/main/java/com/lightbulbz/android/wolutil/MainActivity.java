package com.lightbulbz.android.wolutil;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.lightbulbz.net.MacAddress;
import com.lightbulbz.net.NetUtils;
import com.lightbulbz.net.WOLPacketSender;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.Collection;


public class MainActivity
        extends
            Activity
        implements
            SendWOLFragment.Listener,
            MacAddressFavoritesFragment.Listener,
            CreateFavoriteFragment.OnDialogResultListener {

    private static final String TAG_SEND = "send";
    private static final String TAG_FAVORITES = "favorites";

    private MacAddressFavoritesModel mFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Fragment sendFragment = new SendWOLFragment();
            Fragment favoritesFragment = new MacAddressFavoritesFragment();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.main_container, sendFragment, TAG_SEND);
            if (findViewById(R.id.favorites_container) != null) {
                transaction.add(R.id.favorites_container, favoritesFragment, TAG_FAVORITES);
            } else {
                transaction.add(R.id.main_container, favoritesFragment, TAG_FAVORITES).detach(favoritesFragment);
            }
            transaction.commit();
        }

        try {
            mFavorites = MacAddressFavoritesStorage.readJsonStream(openFileInput("favorites.json"));
        } catch (IOException e) {
            mFavorites = new MacAddressFavoritesModel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        getFragmentManager().removeOnBackStackChangedListener(backStackChangedListener);
        try {
            OutputStream out = openFileOutput("favorites.json", MODE_PRIVATE);
            MacAddressFavoritesStorage.writeJsonStream(mFavorites, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFragmentManager().addOnBackStackChangedListener(backStackChangedListener);
    }

    @Override
    public void onSendRequested(MacAddress target) {
        new SendWOLTask().execute(target);
    }

    @Override
    public void onSaveRequested(MacAddress target) {
        CreateFavoriteFragment.newInstance(target).show(getFragmentManager(), "create");
    }

    private void notifyModelChanged() {
        ((MacAddressFavoritesFragment)getFragmentManager().findFragmentByTag(TAG_FAVORITES)).notifyDataSetChanged();
    }

    @Override
    public boolean canSaveMacAddress() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (findViewById(R.id.favorites_container) == null) {
            Fragment favs = getFragmentManager().findFragmentByTag(TAG_FAVORITES);
            if (favs == null || favs.isDetached()) {
                getMenuInflater().inflate(R.menu.menu_with_favorites, menu);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_item_favorites) {
            Fragment favs = getFragmentManager().findFragmentByTag(TAG_FAVORITES);
            Fragment send = getFragmentManager().findFragmentByTag(TAG_SEND);
            if(favs.isDetached())
            {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                if (!send.isDetached()) {
                    transaction.detach(send);
                }
                transaction
                        .attach(favs)
                        .addToBackStack(null)
                        .commit();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFavoriteSelected(int index) {
        SendWOLFragment send = (SendWOLFragment) getFragmentManager().findFragmentByTag(TAG_SEND);
        send.setMacAddress(mFavorites.getFavorite(index).addr.toString());

        if (findViewById(R.id.favorites_container) == null) {
            getFragmentManager().popBackStackImmediate();
            invalidateOptionsMenu();
        }
    }

    @Override
    public MacAddressFavoritesModel getFavoritesModel() {
        return mFavorites;
    }

    @Override
    public void onRequestDeleteFavorites(Collection<Integer> positions) {
        if (mFavorites != null) {
            mFavorites.removeAll(positions);
        }
    }

    @Override
    public void onCreateFavorite(String name, MacAddress addr) {
        mFavorites.addFavorite(name, addr);
        notifyModelChanged();
    }

    @Override
    public void onNegativeResponse() {

    }

    private class SendWOLTask extends AsyncTask<MacAddress, Void, Void> {

        @Override
        protected Void doInBackground(MacAddress... params) {
            for (InetAddress addr : NetUtils.getBroadcastAddresses()) {
                try {
                    new WOLPacketSender(addr, params[0]).sendPacket();
                }
                catch (java.io.IOException ex) {
                    // ignore it
                }
            }
            return null;
        }
    }

    private final FragmentManager.OnBackStackChangedListener backStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            invalidateOptionsMenu();
        }
    };

}
