package com.example.jassenmoran.simpletodo;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by jassenmoran on 2/9/17.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    private TodoItemClickListener mTodoItemClickListener;

    public interface TodoItemClickListener {
        void onTodoItemClick(long clickedItemIndex);
    }

    public TodoAdapter(Context context, Cursor cursor, TodoItemClickListener clickListener) {
        this.mContext = context;
        this.mCursor = cursor;
        this.mTodoItemClickListener = clickListener;
    }

    @Override
    public TodoAdapter.TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.todo_item, parent, false);
        return new TodoViewHolder(view, mTodoItemClickListener);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position))
            return;
        String title = mCursor.getString(mCursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_TODO_TITLE));
        String priority = mCursor.getString(mCursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_TODO_PRI));
        long id = mCursor.getLong(mCursor.getColumnIndex(TodoContract.TodoEntry._ID));
        holder.mTitleTextView.setText(title);
        holder.mPriorityTextView.setText(priority);
        holder.itemView.setTag(id); // store id - not intended to be displayed
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTitleTextView;
        TextView mPriorityTextView;
        TodoItemClickListener mListener;

        public TodoViewHolder(View itemView, TodoItemClickListener clickListener) {
            super(itemView);
            mListener = clickListener;
            mTitleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
            mPriorityTextView = (TextView) itemView.findViewById(R.id.priority_text_view);

            mTitleTextView.setOnClickListener(this);
            mPriorityTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            long id = (long) itemView.getTag();
            mListener.onTodoItemClick(id);
        }
    }
}
