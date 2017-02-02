package com.example.jassenmoran.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class EditItemActivity extends AppCompatActivity {

    private int pos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String text = getIntent().getStringExtra("item_text");
        pos = getIntent().getIntExtra("pos", 0);

        setContentView(R.layout.activity_edit_item);

        EditText et = (EditText) findViewById(R.id.editText);
        et.setText(text);
        et.requestFocus();
        et.setSelection(et.getText().length());
    }


    public void onSaveItem(View v) {
        EditText et = (EditText) findViewById(R.id.editText);
        String newItemText = et.getText().toString();

        Log.d("Me", "building intent");

        Intent data = new Intent();
        data.putExtra("new_text", newItemText);
        data.putExtra("pos", pos);

        setResult(RESULT_OK, data);
        Log.d("Me", "result set");

        finish();
    }
}
