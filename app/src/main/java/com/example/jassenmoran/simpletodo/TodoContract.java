package com.example.jassenmoran.simpletodo;

import android.provider.BaseColumns;

/**
 * Created by jassenmoran on 2/9/17.
 */

public class TodoContract {

    public static final class TodoEntry implements BaseColumns {
        public static final String TABLE_NAME = "todos";
        public static final String COLUMN_TODO_TITLE = "todoTitle";
        public static final String COLUMN_TODO_MONTH = "month";
        public static final String COLUMN_TODO_DAY = "day";
        public static final String COLUMN_TODO_YEAR = "year";
    }
}
