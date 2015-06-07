package com.lightbulbz.android.wolutil;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import com.lightbulbz.net.MacAddress;
import com.lightbulbz.net.NetUtils;
import com.lightbulbz.net.WOLPacketSender;

import java.net.InetAddress;


public class MainActivity extends Activity implements SendWOLFragment.OnSendRequestedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction().add(R.id.main_container, new SendWOLFragment()).commit();
    }

    @Override
    public void onSendRequested(MacAddress target) {
        new SendWOLTask().execute(target);
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
