package com.example.jassenmoran.simpletodo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity implements TodoAdapter.TodoItemClickListener {

    private RecyclerView mRecyclerView;
    private TodoAdapter mAdapter;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) this.findViewById(R.id.all_todos_list_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        TodoDbHelper dbHelper = new TodoDbHelper(this);

        mDb = dbHelper.getWritableDatabase();
        Cursor cursor = getAllTodos();

        mAdapter = new TodoAdapter(this, cursor, this);
        mRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                // don't care for now
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDirection) {
                long id = (long) viewHolder.itemView.getTag();
                removeTodoItem(id);
                mAdapter.swapCursor(getAllTodos());
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    private Cursor getAllTodos() {
        return mDb.query(
                TodoContract.TodoEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onTodoItemClick(long clickedItemIndex) {

        // intent to bring up edit item activity
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        String[] cols = new String[] {TodoContract.TodoEntry._ID, TodoContract.TodoEntry.COLUMN_TODO_TITLE, TodoContract.TodoEntry.COLUMN_TODO_DATE};
        Cursor qCursor = mDb.query(
                TodoContract.TodoEntry.TABLE_NAME,
                cols,
                TodoContract.TodoEntry._ID + "=" + clickedItemIndex,
                null,
                null,
                null,
                null);

        if (qCursor != null)
        {
            qCursor.moveToFirst();
            i.putExtra("item_text", qCursor.getString(qCursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_TODO_TITLE)));
            i.putExtra("pos",       clickedItemIndex);

//            i.putExtra("pos",       qCursor.getString(qCursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_TODO_DATE)));
            startActivityForResult(i, 0);

            qCursor.close();
        }
    }

    public void removeTodoItem(long id) {
        mDb.delete(TodoContract.TodoEntry.TABLE_NAME,
                   TodoContract.TodoEntry._ID + "=" + id, null);
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();

        addNewTodo(itemText);

        mAdapter.swapCursor(getAllTodos());
        etNewItem.getText().clear();
    }

    private long addNewTodo(String todoTitle) {
        Log.i("me", "Adding " + todoTitle);
        ContentValues cv = new ContentValues();
        cv.put(TodoContract.TodoEntry.COLUMN_TODO_TITLE, todoTitle);
        cv.put(TodoContract.TodoEntry.COLUMN_TODO_DATE, "today");

        return mDb.insert(TodoContract.TodoEntry.TABLE_NAME, null, cv);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Log.d("Me", "received result");
            // Extract new text and the position from result extras
            String newText = data.getExtras().getString("new_text");
            long pos = data.getExtras().getLong("pos", 0);
            int month = data.getExtras().getInt("month", 0);
            int day = data.getExtras().getInt("day", 0);
            int year = data.getExtras().getInt("year", 0);
            Log.d("Me", month + "/" + day + "/" + year);
            saveTodoItem(pos, newText, "new date");
        }
    }

    public void saveTodoItem(long id, String text, String date) {

        ContentValues args = new ContentValues();
        args.put(TodoContract.TodoEntry._ID, id);
        args.put(TodoContract.TodoEntry.COLUMN_TODO_TITLE, text);
        args.put(TodoContract.TodoEntry.COLUMN_TODO_DATE, date);
        int i =  mDb.update(TodoContract.TodoEntry.TABLE_NAME, args, TodoContract.TodoEntry._ID + "=" + id, null);

        mAdapter.swapCursor(getAllTodos());
    }
}
