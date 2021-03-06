package com.example.jassenmoran.simpletodo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements TodoAdapter.TodoItemClickListener,
        EditTodoDialogFragment.EditDialogListener, DeleteConfirmationDialog.DeleteConfirmDialogListener
{
    private RecyclerView   mRecyclerView;
    private TodoAdapter    mAdapter;
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

                FragmentManager fm = getSupportFragmentManager();
                DeleteConfirmationDialog deleteDialogFragment = DeleteConfirmationDialog.newInstance(id);
                deleteDialogFragment.show(fm, "");
            }
        }).attachToRecyclerView(mRecyclerView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();

                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                int year = c.get(Calendar.YEAR);

                showEditDialog(100, "", month, day, year, "Low");
            }
        });
    }

    @Override
    public void onDeleteConfirmed(Long id) {
        removeTodoItem(id);
        mAdapter.swapCursor(getAllTodos());
    }

    @Override
    public void onDeleteCancelled() {
        mAdapter.swapCursor(getAllTodos());
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

        Cursor qCursor = mDb.query(
                TodoContract.TodoEntry.TABLE_NAME,
                null,
                TodoContract.TodoEntry._ID + "=" + clickedItemIndex,
                null,
                null,
                null,
                null);

        if (qCursor != null)
        {
            qCursor.moveToFirst();

            String title = qCursor.getString(qCursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_TODO_TITLE));
            String priority = qCursor.getString(qCursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_TODO_PRI));
            int month = qCursor.getInt(qCursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_TODO_MONTH));
            int day = qCursor.getInt(qCursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_TODO_DAY));
            int year = qCursor.getInt(qCursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_TODO_YEAR));

            showEditDialog(clickedItemIndex, title, month, day, year, priority);

            qCursor.close();
        }
    }


    private void showEditDialog(long index, String text, int month, int day, int year, String priority) {
        FragmentManager fm = getSupportFragmentManager();
        EditTodoDialogFragment editTodoDialogFragment = EditTodoDialogFragment.newInstance(index, text, month, day, year, priority);
        editTodoDialogFragment.show(fm, "ll_edit_todo");
    }


    public void removeTodoItem(long id) {
        mDb.delete(TodoContract.TodoEntry.TABLE_NAME,
                   TodoContract.TodoEntry._ID + "=" + id, null);
    }

    private long addNewTodo(long id, String text, int month, int day, int year, String priority) {
        Log.i("me", "Adding " + text);
        long insertResult = -1;
        if (text.length() > 0) {
            ContentValues cv = new ContentValues();
            cv.put(TodoContract.TodoEntry.COLUMN_TODO_TITLE, text);
            cv.put(TodoContract.TodoEntry.COLUMN_TODO_MONTH, month);
            cv.put(TodoContract.TodoEntry.COLUMN_TODO_DAY, day);
            cv.put(TodoContract.TodoEntry.COLUMN_TODO_YEAR, year);
            cv.put(TodoContract.TodoEntry.COLUMN_TODO_PRI, priority);

            insertResult = mDb.insert(TodoContract.TodoEntry.TABLE_NAME, null, cv);
        }
        return insertResult;

    }

    @Override
    public void onFinishEditDialog(Bundle bundle) {

        saveTodoItem(bundle.getLong("pos"), bundle.getString("title"), bundle.getInt("month"), bundle.getInt("day"), bundle.getInt("year"), bundle.getString("priority"));
    }

    public void saveTodoItem(long id, String text, int month, int day, int year, String priority) {

        ContentValues args = new ContentValues();
        args.put(TodoContract.TodoEntry._ID, id);
        args.put(TodoContract.TodoEntry.COLUMN_TODO_TITLE, text);
        args.put(TodoContract.TodoEntry.COLUMN_TODO_MONTH, month);
        args.put(TodoContract.TodoEntry.COLUMN_TODO_DAY, day);
        args.put(TodoContract.TodoEntry.COLUMN_TODO_YEAR, year);
        args.put(TodoContract.TodoEntry.COLUMN_TODO_PRI, priority);

        int i =  mDb.update(TodoContract.TodoEntry.TABLE_NAME, args, TodoContract.TodoEntry._ID + "=" + id, null);

        if (i < 1)
        {
            addNewTodo(id, text, month, day, year, priority);
        }
        mAdapter.swapCursor(getAllTodos());
    }
}