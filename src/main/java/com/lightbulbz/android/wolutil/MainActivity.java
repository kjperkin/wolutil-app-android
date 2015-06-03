package com.lightbulbz.android.wolutil;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSend)
        {
            (new AlertDialog.Builder(this))
                    .setTitle("Info")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setMessage("Send button clicked.")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

}
