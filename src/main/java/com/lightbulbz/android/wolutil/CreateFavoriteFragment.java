package com.lightbulbz.android.wolutil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lightbulbz.net.MacAddress;

/**
 * Created by kevin on 6/11/15.
 */
public class CreateFavoriteFragment extends DialogFragment {

    private MacAddress mAddr;
    private OnDialogResultListener mListener;

    public static CreateFavoriteFragment newInstance(MacAddress addr) {
        Bundle b = new Bundle();
        b.putByteArray("macAddress", addr.getAddressBytes());
        CreateFavoriteFragment f = new CreateFavoriteFragment();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() == null) {
            throwIllegalArgumentsException();
        }

        byte[] macBytes = getArguments().getByteArray("macAddress");
        if (macBytes == null) {
            throwIllegalArgumentsException();
        }
        mAddr = MacAddress.fromBytes(macBytes);
        super.onCreate(savedInstanceState);
    }

    private void throwIllegalArgumentsException() {
        throw new IllegalArgumentException("CreateFavoriteFragment must have a macAddress argument");
    }

    @Override
    public void onAttach(Activity activity) {
        try {
            mListener = (OnDialogResultListener) activity;
        } catch (ClassCastException ex) {
            throw new ClassCastException(activity.toString()
                    + " must implement CreateFavoriteFragment.OnDialogResultListener");
        }
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    private final DialogInterface.OnClickListener positiveButtonListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String name = ((EditText) getDialog().findViewById(R.id.favoriteName)).getText().toString();
            if (mListener != null) {
                mListener.onCreateFavorite(name, mAddr);
            }
        }
    };

    private final DialogInterface.OnClickListener negativeButtonListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogContents = inflater.inflate(R.layout.fragment_create_favorite, null);
        ((TextView) dialogContents.findViewById(R.id.address)).setText(mAddr.toString());

        return builder
                .setTitle(R.string.title_create_favorite)
                .setPositiveButton(android.R.string.ok, positiveButtonListener)
                .setNegativeButton(android.R.string.cancel, negativeButtonListener)
                .setView(dialogContents)
                .create();
    }

    public interface OnDialogResultListener {
        void onCreateFavorite(String name, MacAddress addr);
        void onNegativeResponse();
    }
}
