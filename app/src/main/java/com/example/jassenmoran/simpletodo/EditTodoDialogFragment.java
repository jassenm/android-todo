package com.example.jassenmoran.simpletodo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.support.v4.app.DialogFragment;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by jassenmoran on 2/16/17.
 */

public class EditTodoDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener,
        AdapterView.OnItemSelectedListener
{

    private String mTitle;
    private int mMonth;
    private int mDay;
    private int mYear;
    private String mPriority;

    private Long mPos;
    EditText mTitleEditText;
    TextView mDate;
    private static final String POSITION = "pos";
    private static final String TITLE = "title";
    private static final String MONTH = "month";
    private static final String DAY = "day";
    private static final String YEAR = "year";
    private static final String PRIORITY = "priority";

    public EditTodoDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public interface EditDialogListener {
        void onFinishEditDialog(Bundle bundle);
    }


    public static EditTodoDialogFragment newInstance(Long index, String text, int month, int day, int year, String priority) {
        EditTodoDialogFragment frag = new EditTodoDialogFragment();
        Bundle bundle = getBundle(index, text, month, day, year, priority);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        Bundle bundle = getArguments();
        mTitle = bundle.getString(TITLE);
        mPos = bundle.getLong(POSITION);
        mMonth = bundle.getInt(MONTH);
        mDay = bundle.getInt(DAY);
        mYear = bundle.getInt(YEAR);
        mPriority = bundle.getString(PRIORITY);

        View theView = inflater.inflate(R.layout.fragment_edit_item, null);

        mTitleEditText = (EditText) theView.findViewById(R.id.et_todo_title);
        mTitleEditText.setText(mTitle);
        mTitleEditText.requestFocus();

        mDate = (TextView) theView.findViewById(R.id.tv_date);
        mDate.setText(formatDate(mMonth, mDay, mYear));

        mDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showDatePickerDialog();
            }
        });

        Spinner spinner = (Spinner) theView.findViewById(R.id.priority_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(mPriority));
        spinner.setOnItemSelectedListener(this);

        builder.setView(theView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // pass info
                        Bundle returnedBundle = getBundle(mPos, mTitleEditText.getText().toString(), mMonth, mDay, mYear, mPriority);

                        EditDialogListener listener = (EditDialogListener) getActivity();
                        listener.onFinishEditDialog(returnedBundle);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        return builder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        mPriority = parent.getItemAtPosition(pos).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @NonNull
    private static Bundle getBundle(Long mPos, String value, int mMonth, int mDay, int mYear, String priority) {
        Bundle returnedBundle = new Bundle();
        returnedBundle.putLong(POSITION, mPos);
        returnedBundle.putString(TITLE, value);
        returnedBundle.putInt(MONTH, mMonth);
        returnedBundle.putInt(DAY, mDay);
        returnedBundle.putInt(YEAR, mYear);
        returnedBundle.putString(PRIORITY, priority);
        return returnedBundle;
    }


    public void showDatePickerDialog() {

        DatePickerDialogFragment newFragment = new DatePickerDialogFragment();

        Bundle bundle = getArguments();
        mMonth = bundle.getInt(MONTH);
        mDay = bundle.getInt(DAY);
        mYear = bundle.getInt(YEAR);

        newFragment.setArguments(bundle);
        newFragment.setCallback(this);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        mMonth = month;
        mDay = day;
        mYear = year;
        mDate.setText(formatDate(mMonth, mDay, mYear));
    }

    private String formatDate(int month, int day, int year)
    {
        month = month+1;
        return month + "/" + day + "/" + year;
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }
}