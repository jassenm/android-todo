package com.example.jassenmoran.simpletodo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

/**
 * Created by jassenmoran on 2/9/17.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        public interface OnSetListener {
            void OnSet(int year, int month, int day);
        }

        private OnSetListener mListener;

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            try {
                this.mListener = (OnSetListener)activity;
            }
            catch (final ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement OnSetListener");
            }
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            return new DatePickerDialog(getActivity(), this, getArguments().getInt("year"),
                    getArguments().getInt("month"), getArguments().getInt("day"));
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            // save with text
            this.mListener.OnSet(month, day, year);
        }
    }
