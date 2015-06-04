package com.lightbulbz.android.wolutil;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lightbulbz.net.MacAddress;
import com.lightbulbz.net.MacAddressFormatException;
import com.lightbulbz.net.NetUtils;
import com.lightbulbz.net.WOLPacketSender;

import java.net.InetAddress;


public class MainActivity extends Activity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendButton = (Button) findViewById(R.id.buttonSend);
        sendButton.setOnClickListener(this);
        final EditText macAddress = (EditText) findViewById(R.id.textView);
        macAddress.addTextChangedListener(new MyTextWatcher(this, macAddress, "(?:[0-9a-fA-F]{2}+:){5}+[0-9a-fA-F]{2}+"));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSend)
        {
            EditText et = (EditText) findViewById(R.id.textView);
            try {
                MacAddress addr = MacAddress.parseMacAddress(et.getText().toString());
                new SendWOLTask().execute(addr);
            }
            catch (MacAddressFormatException ex)
            {
                Log.w("wolutil-android", "Invalid mac address: " + et.getText().toString());
            }
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
