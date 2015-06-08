package com.lightbulbz.android.wolutil;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lightbulbz.net.MacAddress;
import com.lightbulbz.net.MacAddressFormatException;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendWOLFragment extends Fragment implements View.OnClickListener {


    private static final String KEY_MAC_ADDRESS = "SendWOLFragment.macAddress";
    private EditText mMacAddress;
    private MyTextWatcher mTextWatcher;
    private OnSendRequestedListener mListener;
    private String mTempAddressString;

    public SendWOLFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSendRequestedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement interface OnSendRequestedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_send_wol, container, false);
        myView.findViewById(R.id.buttonSend).setOnClickListener(this);

        mMacAddress = (EditText) myView.findViewById(R.id.textView);
        mTextWatcher = new MyTextWatcher(getActivity(), mMacAddress, "(?:[0-9a-fA-F]{2}+:){5}+[0-9a-fA-F]{2}+");
        mMacAddress.addTextChangedListener(mTextWatcher);
        if (savedInstanceState != null) {
            mTempAddressString = savedInstanceState.getString(KEY_MAC_ADDRESS);
        }

        return myView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mTempAddressString != null) {
            outState.putString(KEY_MAC_ADDRESS, mTempAddressString);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (mTempAddressString != null && mMacAddress != null) {
            // We had a mac address set while our view was destroyed.
            // We should put it into effect now.
            mMacAddress.setText(mTempAddressString);
            mTempAddressString = null;
        }
    }

    @Override
    public void onDestroyView() {
        mMacAddress.removeTextChangedListener(mTextWatcher);
        mTextWatcher = null;
        mMacAddress = null;
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSend)
        {
            if (mMacAddress != null) {
                try {
                    MacAddress addr = MacAddress.parseMacAddress(mMacAddress.getText().toString());
                    if (mListener != null) {
                        mListener.onSendRequested(addr);
                    }
                } catch (MacAddressFormatException ex) {
                    Log.w("SendWOLFragment", "Invalid mac address: " + mMacAddress.getText().toString());
                }
            } else {
                Log.e("SendWOLFragment", "onClick called but view seems to have been destroyed!");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setMacAddress(String address) {
        if (mMacAddress != null) {
            mMacAddress.setText(address);
        } else {
            // If mMacAddress is null, we don't have a view yet.
            // Save the address and put it in the view when we create it
            mTempAddressString = address;
        }
    }

    public interface OnSendRequestedListener {
        void onSendRequested(MacAddress target);
    }
}
