package com.example.jassenmoran.simpletodo;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import java.util.Calendar;


public class EditItemActivity extends AppCompatActivity implements DatePickerFragment.OnSetListener {

    private long pos = -1;
    String itemText;
    int month = 0;
    int day = 0;
    int year = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.edit_activity_name);
        final Calendar c = Calendar.getInstance();

        Intent intentThatStartedActivity = getIntent();
        if (intentThatStartedActivity.hasExtra("item_text")) {
            itemText = intentThatStartedActivity.getStringExtra("item_text");
        }
        if (intentThatStartedActivity.hasExtra("pos")) {
            pos = intentThatStartedActivity.getLongExtra("pos", 0);
        }
        if (intentThatStartedActivity.hasExtra("month")) {
            month = intentThatStartedActivity.getIntExtra("month", 0);
        }
        else {
            month = c.get(Calendar.MONTH) + 1;
        }
        if (intentThatStartedActivity.hasExtra("day")) {
            day = intentThatStartedActivity.getIntExtra("day", 0);
        }
        else {
            day = c.get(Calendar.DAY_OF_MONTH);
        }
        if (intentThatStartedActivity.hasExtra("year")) {
            year = intentThatStartedActivity.getIntExtra("year", 0);
        }
        else {
            year = c.get(Calendar.YEAR);
        }

        setContentView(R.layout.activity_edit_item);

        EditText et = (EditText) findViewById(R.id.editText);
        et.setText(itemText);
        et.requestFocus();
        et.setSelection(et.getText().length());

        TextView tv = (TextView) findViewById(R.id.DateTextView);
        tv.setText(month + "/" + day + "/" + year);
    }

    public void onSaveItem(View v) {
        EditText et = (EditText) findViewById(R.id.editText);
        String newItemText = et.getText().toString();

        Intent data = new Intent();
        data.putExtra("new_text", newItemText);
        data.putExtra("pos", pos);
        data.putExtra("month", month);
        data.putExtra("day", day);
        data.putExtra("year", year);

        setResult(RESULT_OK, data);

        finish();
    }

    public void onClickDate(View v) {
        showDatePickerDialog();
    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("month", month);
        bundle.putInt("day", day);
        bundle.putInt("year", year);

        newFragment.setArguments(bundle);

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void OnSet(int month, int day, int year) {
        this.month = month;
        this.day = day;
        this.year = year;
    }
}
