package com.example.jassenmoran.simpletodo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;


/**
 * Created by jassenmoran on 2/20/17.
 */

public class DatePickerDialogFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener mListener = null;

    public DatePickerDialogFragment() {
        // nothing
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Bundle bundle = getArguments();
        return new DatePickerDialog(getActivity(), mListener, bundle.getInt("year"), bundle.getInt("month"), bundle.getInt("day"));
    }

    public void setCallback(DatePickerDialog.OnDateSetListener listener) {
        mListener = listener;
    }
}
