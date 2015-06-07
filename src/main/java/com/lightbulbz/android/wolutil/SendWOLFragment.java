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


    private EditText mMacAddress;
    private MyTextWatcher mTextWatcher;
    private OnSendRequestedListener mListener;

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
        return myView;
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
        }
    }

    public interface OnSendRequestedListener {
        void onSendRequested(MacAddress target);
    }
}
