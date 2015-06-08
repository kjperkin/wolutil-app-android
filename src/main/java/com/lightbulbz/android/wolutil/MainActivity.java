package com.lightbulbz.android.wolutil;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.lightbulbz.net.MacAddress;
import com.lightbulbz.net.NetUtils;
import com.lightbulbz.net.WOLPacketSender;

import java.net.InetAddress;


public class MainActivity extends Activity implements SendWOLFragment.OnSendRequestedListener, MacAddressFavoritesFragment.OnMacAddressSelectedListener {

    public static final String TAG_SEND = "send";
    public static final String TAG_FAVORITES = "favorites";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Fragment sendFragment = new SendWOLFragment();
            Fragment favoritesFragment = MacAddressFavoritesFragment.newInstance("");

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.main_container, sendFragment, TAG_SEND);
            if (findViewById(R.id.favorites_container) != null) {
                transaction.add(R.id.favorites_container, favoritesFragment, TAG_FAVORITES);
            } else {
                transaction.add(R.id.main_container, favoritesFragment, TAG_FAVORITES).detach(favoritesFragment);
            }
            transaction.commit();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        getFragmentManager().removeOnBackStackChangedListener(backStackChangedListener);
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
    public void onFavoriteSelected(MacAddressFavoritesModel.Favorite favorite) {
        SendWOLFragment send = (SendWOLFragment) getFragmentManager().findFragmentByTag(TAG_SEND);
        send.setMacAddress(favorite.addr.toString());

        if (findViewById(R.id.favorites_container) == null) {
            getFragmentManager().popBackStackImmediate();
            invalidateOptionsMenu();
        }
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

    private FragmentManager.OnBackStackChangedListener backStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            invalidateOptionsMenu();
        }
    };

}
