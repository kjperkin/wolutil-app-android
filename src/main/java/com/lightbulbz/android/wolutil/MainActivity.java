package com.lightbulbz.android.wolutil;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.lightbulbz.net.MacAddress;
import com.lightbulbz.net.NetUtils;
import com.lightbulbz.net.WOLPacketSender;

import java.net.InetAddress;


public class MainActivity extends Activity implements SendWOLFragment.OnSendRequestedListener, MacAddressFavoritesFragment.OnMacAddressSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager()
                .beginTransaction()
                .add(R.id.main_container, new SendWOLFragment(), "send")
                .add(MacAddressFavoritesFragment.newInstance(""), "favorites")
                .commit();
    }

    @Override
    public void onSendRequested(MacAddress target) {
        new SendWOLTask().execute(target);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void showFavorites() {
        Fragment f = getFragmentManager().findFragmentByTag("favorites");
        if (f == null) {
            f = MacAddressFavoritesFragment.newInstance("");
        }

        getFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .remove(getFragmentManager().findFragmentByTag("send"))
                .add(R.id.main_container, f, "favorites").commit();
    }

    @Override
    public void onFavoriteSelected(MacAddressFavoritesModel.Favorite favorite) {
        getFragmentManager().popBackStack();
        hideFavorites();
        showSend();
        ((SendWOLFragment) getFragmentManager().findFragmentByTag("send")).setMacAddress(favorite.addr.toString());
    }

    private void showSend() {
        Fragment f = getFragmentManager().findFragmentByTag("send");
        if (f == null) {
            f = new SendWOLFragment();
        }
        getFragmentManager()
                .beginTransaction()
                .add(R.id.main_container, f, "send")
                .commit();
        invalidateOptionsMenu();
    }

    private void hideFavorites() {
        Fragment f = getFragmentManager().findFragmentByTag("favorites");
        if (f != null && f.isVisible()) {
            getFragmentManager().beginTransaction().remove(f).commit();
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
}
