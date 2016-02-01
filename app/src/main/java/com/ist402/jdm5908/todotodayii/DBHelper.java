package com.ist402.jdm5908.todotodayii;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper{

    // Defining the database and table
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "toDo_Today";
    private static final String DATABASE_TABLE = "toDo_Items";

    // Defining the column names for the table
    private static final String KEY_TASK_ID = "_id";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IS_DONE = "is_done";

    private int taskCount;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        String table =
                "CREATE TABLE " + DATABASE_TABLE + "("
                + KEY_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_DESCRIPTION + " TEXT, "
                + KEY_IS_DONE + " INTEGER" + ")";
        db.execSQL(table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){

        // Drop old table if exists
        database.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

        // Recreate table
        onCreate(database);
    }

    //************************ DATABASE OPERATIONS: ADD, EDIT, DELETE
    // Adding new task
    public void addToDoItem(ToDo_Item task){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Adding description value
        values.put(KEY_DESCRIPTION, task.getDescription());

        // Adding is_done value
        values.put(KEY_IS_DONE, task.getIs_done());

        // Inserting row into table
        db.insert(DATABASE_TABLE, null, values);
        taskCount++;

        // Closing connection
        db.close();
    }

    public List<ToDo_Item> getAllTasks(){

        // Get all the task items in the list
        List<ToDo_Item> toDoList = new ArrayList<>();

        // SELECT ALL query
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through tasks
        if (cursor.moveToFirst()){

            do{
                ToDo_Item task = new ToDo_Item();
                task.set_id(cursor.getInt(0));
                task.setDescription(cursor.getString(1));
                task.setIs_done(cursor.getInt(2));
                toDoList.add(task);
            }
            while (cursor.moveToNext());
        }
        // Returning list of tasks
        return toDoList;
    }

    public void clearAll(List<ToDo_Item> list){

        // Get all the list task items and clear
        list.clear();

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, null, new String[]{});
        db.close();
    }

    public void updateTask(ToDo_Item task){

        // Updating record
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_DESCRIPTION, task.getDescription());
        values.put(KEY_IS_DONE, task.getIs_done());

        db.update(DATABASE_TABLE, values, KEY_TASK_ID + " = ?",
                new String[]{String.valueOf(task.get_id())});
        db.close();
    }
}
